package com.nnte.pf_uti_client.utils;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private static JsonFactory jsonFactory = new JsonFactory();

    static {
        //配置该objectMapper在反序列化时，忽略目标对象没有的属性。凡是使用该objectMapper反序列化时，都会拥有该特性。
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static class JNode {
        private JsonNode node;

        public JNode() {
        }

        public JNode(JsonNode objNode) {
            setNode(objNode);
        }

        public JsonNode getNode() {
            return node;
        }

        public void setNode(JsonNode node) {
            this.node = node;
        }

        public Object get(String field) {
            if (node == null)
                return null;
            JsonNode fNode = node.get(field);
            if (fNode == null)
                return null;
            switch (fNode.getNodeType()) {
                case STRING:
                    return fNode.textValue();
                case NUMBER: {
                    if (fNode.isBigDecimal())
                        return fNode.decimalValue();
                    if (fNode.isBigInteger())
                        return fNode.bigIntegerValue();
                    if (fNode.isDouble())
                        return fNode.doubleValue();
                    if (fNode.isFloat())
                        return fNode.floatValue();
                    if (fNode.isInt())
                        return fNode.intValue();
                    if (fNode.isIntegralNumber())
                        return fNode.intValue();
                }
                case BOOLEAN:
                    return fNode.booleanValue();
                case OBJECT:
                    return fNode;
                case ARRAY:
                    return fNode;
            }
            return null;
        }

        public String getText(String field) {
            JsonNode fNode = node.get(field);
            if (fNode == null)
                return null;
            return fNode.textValue();
        }

        public Integer getInteger(String field) {
            JsonNode fNode = node.get(field);
            if (fNode == null)
                return null;
            if (fNode.isIntegralNumber() || fNode.isInt() ||
                    fNode.isBigInteger() || fNode.isNumber())
                return fNode.intValue();
            String sval = fNode.textValue();
            if (sval == null || sval.equals("") || sval.toLowerCase().equals("null"))
                return null;
            return NumberUtil.getDefaultInteger(sval);
        }

        public Double getDouble(String field) {
            JsonNode fNode = node.get(field);
            if (fNode == null)
                return null;
            return fNode.doubleValue();
        }

        public Boolean getBoolean(String field) {
            JsonNode fNode = node.get(field);
            if (fNode == null)
                return null;
            return fNode.booleanValue();
        }
    }

    public static JNode createJNode(JsonNode node) {
        return new JNode(node);
    }

    public static ObjectNode newJsonNode() {
        return objectMapper.createObjectNode();
    }

    /**
     * 对象转换成字符串
     *
     * @param obj
     * @return
     */
    public static String beanToJson(Object obj)  throws Exception {
        return beanToJsonNode(obj).toString();
    }

    /**
     * bean对象转成JsonNode对象
     */
    public static JsonNode beanToJsonNode(Object obj) throws Exception {
        return objectMapper.valueToTree(obj);
    }

    /**
     * json文本反序列化为JsonNode
     */
    public static JsonNode jsonToNode(String json) throws Exception {
        return objectMapper.readTree(json);
    }

    /**
     * 如果jsonNote是序列，返回List<JsonNode>结构
     */
    public static List<JsonNode> noteToNodeArray(JsonNode jsonNote) {
        List<JsonNode> retList = new ArrayList<>();
        if (jsonNote.isArray())
            jsonNote.forEach(e -> {
                retList.add(e);
            });
        else
            retList.add(jsonNote);
        return retList;
    }

    /**
     * json文本如果是序列，返回List<JsonNode>结构
     */
    public static List<JsonNode> jsonToNodeArray(String json) throws Exception {
        if (StringUtils.isEmpty(json))
            return null;
        JsonNode jArrayNote = jsonToNode(json);
        return noteToNodeArray(jArrayNote);
    }

    /**
     * json文本反序列化为java bean
     */
    public static <T> T jsonToBean(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(jsonFactory.createParser(json), clazz);
    }

    /**
     * Enum对象序列化类,该Enum为值必须为Int型，且必须实现接口EnumJsonInterface
     */
    public static class EnumJsonSerializer extends JsonSerializer<EnumJsonInterface> {
        public EnumJsonSerializer() {
        }

        @Override
        public void serialize(EnumJsonInterface enumInterface, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            if (enumInterface != null) {
                try {
                    JsonNode listNode = beanToJsonNode(enumInterface.getValue());
                    jsonGenerator.writeObject(listNode);
                }catch (Exception e){
                    throw new IOException(e.getMessage());
                }
            }
        }
    }

    /**
     * Enum对象反序列化类,该Enum为值必须为Int型，
     * 且必须提供静态函数getEnumFromInt用于通过Int型的输入值取得Enum值
     * getEnumFromInt函数应定义为：public static Object getEnumFromInt(Integer iVal) {}
     */

    public static class EnumJsonDeserializer<T> extends JsonDeserializer<T> {
        public EnumJsonDeserializer() {
        }

        @Override
        public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            String txt = jsonParser.getText();
            Object obj = deserializationContext.getParser().getCurrentValue();
            String name = deserializationContext.getParser().getCurrentName();
            try {
                Field nameFeild = obj.getClass().getDeclaredField(name);
                if (nameFeild != null) {
                    Class propClass = nameFeild.getType();
                    Method getEnumFromInt = propClass.getDeclaredMethod("getEnumFromInt", Integer.class);
                    return (T) getEnumFromInt.invoke(null, Integer.valueOf(txt));
                }
            }catch (Exception e){
                throw new IOException(e.getMessage());
            }
            return null;
        }
    }


    /**
     * 通过MAP对象生成JsonNode
     */
    public static JsonNode fromMap(Map<String, Object> map) throws Exception {
        if (map != null) {
            JsonNode node = objectMapper.valueToTree(map);
            return node;
        }
        return null;
    }

    /**
     * 通过JsonNode生成ObjectNode
     */
    public static ObjectNode getObjectNodefromBean(Object bean) throws Exception {
        if (bean == null)
            return null;
        ObjectNode node = newJsonNode();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // 设置属性是可以访问的
            String fName = field.getName();
            Object val = field.get(bean);
            if (val != null) {
                if (val instanceof Date) {
                    DateUtils.dateToString((Date) val, DateUtils.DF_YMDHMS);
                } else
                    node.put(fName, val.toString());
            }
        }
        return node;
    }

    public static ObjectNode jsonNode2ObjectNode(JsonNode jsonNode) throws Exception{
        return string2ObjectNode(jsonNode.toString());
    }

    public static ObjectNode string2ObjectNode(String jsonString) throws Exception{
        return (ObjectNode) new ObjectMapper().readTree(jsonString);
    }

    /**
     * 通过JsonNode对象生成MAP
     */
    public static Map<String, Object> toMap(JsonNode jsonNode) throws Exception {
        if (jsonNode != null) {
            Map<String, Object> map = objectMapper.convertValue(jsonNode, Map.class);
            return map;
        }
        return null;
    }
/*
	public enum enumTest implements EnumJsonInterface{
		EA(1),EB(2),EC(3);
		private Integer val;

		enumTest(){
			val=1;
		}

		enumTest(int v){
			val=v;
		}

		@Override
		public Object getValue() {
			return val;
		}

		public static Object getEnumFromInt(Integer iVal) {
			switch (iVal){
				case 1:return EA;
				case 2:return EB;
				case 3:return EC;
			}
			return null;
		}
	}

	public enum CircleItemType {
		CIT_QueryFeild,     //查询字段
		CIT_EnvData,        //上下文环境数据
		CIT_NormalTxt;      //普通文本
		private CircleItemType() { }
	}

	public static class SubSubClass{
		public SubSubClass(){ }
		public SubSubClass(String ssname,String ssid){
			setSubsubname(ssname);
			setSubsubid(ssid);
		}
		public String subsubname;
		public String subsubid;

		public String getSubsubname() {
			return subsubname;
		}

		public void setSubsubname(String subsubname) {
			this.subsubname = subsubname;
		}

		public String getSubsubid() {
			return subsubid;
		}

		public void setSubsubid(String subsubid) {
			this.subsubid = subsubid;
		}
	}

	public static class SubClass{
		private String name;
		private Integer iid;

		@JsonDeserialize(as=List.class,contentAs = SubSubClass.class)
		private List<SubSubClass> ssList;

		public SubClass(){ }
		public SubClass(String name,Integer iid){
			setIid(iid);
			setName(name);
		}
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }
		public Integer getIid() { return iid;}
		public void setIid(Integer iid) {  this.iid = iid; }

		public void setSsList(List<SubSubClass> ssList) {
			this.ssList = ssList;
		}
		public List<SubSubClass> getSsList() {
			return this.ssList;
		}
	}

	public static class TestClass{
		private String name;
		private Integer id;

		@JsonDeserialize(using=EnumJsonDeserializer.class)
		@JsonSerialize(using=EnumJsonSerializer.class)
		private enumTest e;

		private CircleItemType enumCIT;

		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
		private Date date;

		@JsonDeserialize(as = List.class,contentAs = SubClass.class)
		private List<SubClass> subClassList;

		public String getName() { return name; }
		public void setName(String name) { this.name = name;}
		public void setId(Integer id) {	this.id = id;}
		public Integer getId() {return id; }
		public enumTest getE() { return e; }
		public void setE(enumTest e) { this.e = e; }
		public Date getDate() { return date; }
		public void setDate(Date date) { this.date = date; }

        TestClass(){}
		TestClass(String name,Integer id,enumTest e,Date d){
			setName(name);
			setId(id);
			setE(e);
			setDate(d);
			enumCIT = CircleItemType.CIT_NormalTxt;
		}

		public CircleItemType getEnumCIT() {
			return enumCIT;
		}

		public void setEnumCIT(CircleItemType enumCIT) {
			this.enumCIT = enumCIT;
		}
	}

	public static void main(String[] args){
		Date d=new Date();
		TestClass tc1=new TestClass("123",1,enumTest.EC,d);
		TestClass tc2=new TestClass("23",2,enumTest.EB,d);
		TestClass tc3=new TestClass("3",3,enumTest.EC,d);
        SubClass sc1=new SubClass("subc1",1);
        SubClass sc2=new SubClass("subc2",2);
		SubSubClass ss1=new SubSubClass("ss1_n","ss1");
		SubSubClass ss2=new SubSubClass("ss2_n","ss3");
		SubSubClass ss3=new SubSubClass("ss3_n","ss3");
		List<SubSubClass> ssL1=new ArrayList<>();
		ssL1.add(ss1);
		ssL1.add(ss2);
		sc1.setSsList(ssL1);
		List<SubSubClass> ssL2=new ArrayList<>();
		ssL2.add(ss3);
		sc2.setSsList(ssL2);
        tc1.subClassList=new ArrayList<>();
        tc1.subClassList.add(sc1);
        tc1.subClassList.add(sc2);

		JsonNode jnTc1=beanToJsonNode(tc1);
		if (jnTc1!=null)
			System.out.println(jnTc1.toString());
		String json=jnTc1.toString();

		TestClass desTc=jsonToBean(json,TestClass.class);
		if (desTc!=null){
			System.out.println(beanToJsonNode(desTc).toString());
		}

		Map<String,Object> tmap=new HashMap<>();
        tmap.put("name","name1");
        tmap.put("id",1);
        tmap.put("date",new Date());
        Map<String,Object> submap=new HashMap<>();
        submap.put("subname","subname1");
        submap.put("subid",1);
        tmap.put("submap",submap);
        List<KeyValue> kvList=new ArrayList<>();
        kvList.add(new KeyValue("key1","value1"));
        kvList.add(new KeyValue("key2","value2"));
        kvList.add(new KeyValue("key3","value3"));
        tmap.put("kvList",kvList);

        JsonNode node=fromMap(tmap);
        System.out.println(node.toString());
        Map map=toMap(node);
        System.out.println(map.toString());
	}*/
}

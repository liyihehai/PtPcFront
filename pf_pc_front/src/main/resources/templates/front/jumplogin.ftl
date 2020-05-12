<!DOCTYPE html>
<html lang="zh-cn">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="renderer" content="webkit">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>跳转登录......</title>
</head>

<body>
  <div class="main container-fluid">
    <div class="container main-cont">
      跳转登录......
    </div>
  </div>

<script type="text/javascript">
    window.onload=function(){
        parent.window.location.href='${envData.contextPath!''}/main/login';
    }
</script>
</body>
</html>

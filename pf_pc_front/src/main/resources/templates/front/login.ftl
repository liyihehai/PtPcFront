<!DOCTYPE html>
<html lang="zh-cn">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="renderer" content="webkit">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>千积变商户端登录</title>
  <meta name="keywords" content="千积变">
  <meta name="description" content="千积变商城">
  <meta http_equiv="pragma" content="no-cache">
  <meta http_equiv="Cache-Control" content="no-cache, must-revalidate">
  <link rel="stylesheet" href="${envData.staticRoot!''}/resources/bootstrap/css/bootstrap.min.css?v=1.2">
  <link rel="stylesheet" href="${envData.staticRoot!''}/resources/bootstrap/css/font-awesome.min.css">
  <link rel="stylesheet" href="${envData.staticRoot!''}/resources/bootstrap/css/ionicons.min.css">
  <link rel="stylesheet" href="${envData.staticRoot!''}/resources/plugins/bootstrap-dialog/1.34.5/bootstrap-dialog.min.css">
  <link rel="stylesheet" href="${envData.staticRoot}/css/global.css">
</head>

<body>
<header class="toper">
  <div class="container">
    <img src="${envData.staticRoot}/images/logo.jpg"><i>|</i><span>商户登录</span>
  </div>
</header>
<form action="" name="loginForm" id="loginForm_id" method="post">
  <div class="main container-fluid">
    <div class="container main-cont">
      <div class="banner-text"><img src="${envData.staticRoot}/images/text.png"></div>
      <div class="banner-form">
        <div class="form">
          <div class="form-title">千积变</div>
          <div class="form-txt">积少成多也是一种优秀</div>
          <input type="hidden" name="aimPwd" id="aimPwd">
          <ul class="form-ul">
            <li><input class="input-wp" maxlength="20" type="tel" placeholder="请输入手机号或账号" name="userName" id="userName" value="">
              <i class="icon-32 icon-32-phone"></i></li>
            <li><input class="input-wp" minlength="6" maxlength="20" type="password" placeholder="请输入密码" name="password" id="password"><i class="icon-32 icon-32-lock"></i></li>
            <li class="form-radio clearfix" onClick="submit_me();">
              <input type="checkbox" id="isRemember"><span>记住我</span>
              <div class="login-btn" id="popAuth">登录</div>
            </li>
          </ul>
          <!-- <div class="form-or">-or-</div> -->
          <div class="form-join clearfix">
            <a class="pull-left" href="/w/fogot.action">忘记密码？</a>
            <div class="form-or">- or -</div>
            <a class="pull-right" href="/w/register.action">注册</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</form>
<!-- 底部导航 -->
<footer class="footer">
  <div class="container clearfix">
    <div class="popup-backdrop" style="display:none;"></div>
  </div>
</footer>
<script src="${envData.staticRoot}/resources/plugins/jQuery/jQuery-2.1.4.min.js"></script>
<script src="${envData.staticRoot!''}/resources/bootstrap/js/bootstrap.min.js"></script>
<script src="${envData.staticRoot!''}/resources/plugins/bootstrap-dialog/1.34.5/bootstrap-dialog.min.js"></script>
<script src="${envData.staticRoot}/js/common.js?v=1.1.9"></script>
<script src="${envData.staticRoot}/js/utils/crypto-js-4.0.0/crypto-js.js"></script>
<script src="${envData.staticRoot}/js/utils/crypto-js-4.0.0/aes.js"></script>
<script src="${envData.staticRoot}/js/secret.js"></script>
<script src="${envData.staticRoot}/js/login.s.js?v=1.18"></script>
<script type="text/javascript">
    function submit_me(){
        loginModle.submitLoginInfo($("#userName").val(),$("#password").val());
    }

    $(function () {
        var gAjax = new AppJSGlobAjax();
        gAjax.setSessionStorageItem("contextPath","${envData.contextPath!''}");
        <#if (map.message??) >
        msgbox.showMsgBox("${map.message!''}");
        </#if>
        var defUser=gAjax.getLocalStorageItem("defaultUserName");
        if (defUser)
            $("#userName").val(defUser);
    })

</script>
</body>
</html>

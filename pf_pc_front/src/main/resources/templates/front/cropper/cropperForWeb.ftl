<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="x-ua-compatible" content="ie=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="图片剪裁上传">
	<meta name="keywords" content="图片上传">
	<meta name="author" content="author">
	<title>图片剪裁上传</title>
	<link rel="apple-touch-icon" href="${envData.staticRoot}/js/utils/upload/css/image/apple-touch-icon.png">
	<link rel="shortcut icon" href="${envData.staticRoot}/js/utils/upload/css/image/favicon.ico">
	<link rel="icon" href="${envData.staticRoot}/js/utils/upload/css/image/favicon.ico">
	<link rel="stylesheet" href="${envData.staticRoot}/js/utils/upload/css/font-awesome.min.css">
	<link rel="stylesheet" href="${envData.staticRoot}/js/utils/upload/css/bootstrap.min.css">
	<link rel="stylesheet" href="${envData.staticRoot}/js/utils/upload/css/cropper.min.css">
	<link rel="stylesheet" href="${envData.staticRoot}/js/utils/upload/css/main.css">
</head>
<body>
<div class="modal-dialog" style="max-width:640px;min-width:320px; margin:0px;">
	<div class="modal-content">
		<div class="modal-header" style="padding: 10px;">
			<button type="button" class="close" onclick="cropperWnd.closeCropperDialog('')" style="margin-top: 2px;" >×</button>
			<h4 class="modal-title" id="getCroppedCanvasTitle">图片剪裁上传</h4>
		</div>
		<div class="modal-body">
			<div class="container-fluid">
				<div class="row">
					<div class="row-content">
						<div class="img-container" style="border: 1px solid #ccc; background: url(${envData.staticRoot}/js/utils/upload/css/image/plus.png) no-repeat 50% 50%;background-size:30%;">
							<img src="" alt="" class="cropper-hidden">
						</div>
					</div>
					<div class="row-preview visible-xs">
						<div class="docs-preview clearfix">
							<div class="img-preview preview-lg"><img src="" style="display: block; width: 100%; height: 100%; min-width: 0px !important; min-height: 0px !important; max-width: none !important; max-height: none !important; transform: rotate(0deg) scale(1, 1);"></div>
						</div>
						<div class="docs-data">
							<div class="input-group input-group-sm">
								<label class="input-group-addon" for="dataX">X</label>
								<input type="text" class="form-control" data-method="move" id="dataX" placeholder="x">
								<span class="input-group-addon">px</span>
							</div>
							<div class="input-group input-group-sm">
								<label class="input-group-addon" for="dataY">Y</label>
								<input type="text" class="form-control" data-method="move" id="dataY" placeholder="y">
								<span class="input-group-addon">px</span>
							</div>
							<div class="input-group input-group-sm">
								<label class="input-group-addon" for="dataWidth">宽</label>
								<input type="text" class="form-control" data-method="setCropBoxData" id="dataWidth" placeholder="width">
								<span class="input-group-addon">px</span>
							</div>
							<div class="input-group input-group-sm">
								<label class="input-group-addon" for="dataHeight">高</label>
								<input type="text" class="form-control" data-method="setCropBoxData" id="dataHeight" placeholder="height">
								<span class="input-group-addon">px</span>
							</div>
							<div class="input-group input-group-sm">
								<label class="input-group-addon" for="dataRotate">转</label>
								<input type="text" class="form-control" data-method="rotate" id="dataRotate" placeholder="rotate">
								<span class="input-group-addon">度</span>
							</div>
						</div>
					</div>
				</div>
				<div class="row" id="actions">
					<div class="docs-buttons">
						<div class="btn-group docs-toggles" data-toggle="buttons" style="display: none;">
							<label class="btn">宽高比例： </label>
							<label class="btn btn-primary" data-toggle="tooltip" title="宽高比: 16 / 9">
								<input type="radio" class="sr-only" id="aspectRatio1" name="aspectRatio" value="1.7777777777777777">
								<span class="docs-tooltip">
				              16:9
				            </span>
							</label>
							<label class="btn btn-primary" data-toggle="tooltip" title="宽高比: 4 / 3">
								<input type="radio" class="sr-only" id="aspectRatio2" name="aspectRatio" value="1.3333333333333333">
								<span class="docs-tooltip">
				              4:3
				            </span>
							</label>
							<label class="btn btn-primary" data-toggle="tooltip" title="宽高比: 1 / 1">
								<input type="radio" class="sr-only" id="aspectRatio3" name="aspectRatio" value="1">
								<span class="docs-tooltip">
				              1:1
				            </span>
							</label>
						</div>

						<div class="btn-group">
							<label class="btn btn-primary btn-upload" for="inputImage" data-toggle="tooltip" title="选择图片进行裁剪">
								<input type="file" class="sr-only" id="inputImage" name="inputImage" accept="image/*">
								<span class="docs-tooltip">
				              <span class="fa fa-upload"> 选择图片</span>
				            </span>
							</label>

							<button type="button" class="btn btn-primary btn-state-toggle" data-method="reset" data-toggle="tooltip" title="重置图片">
				            <span class="docs-tooltip">
				              <span class="fa fa-refresh"> 重置</span>
				            </span>
							</button>
						</div>

						<div class="btn-group">
							<button type="button" class="btn btn-primary btn-state-toggle" data-method="zoom" data-option="0.1" data-toggle="tooltip" title="放大图片">
				            <span class="docs-tooltip">
				              <span class="fa fa-search-plus"></span>
				            </span>
							</button>
							<button type="button" class="btn btn-primary btn-state-toggle" data-method="zoom" data-option="-0.1" data-toggle="tooltip" title="缩小图片" >
				            <span class="docs-tooltip">
				              <span class="fa fa-search-minus"></span>
				            </span>
							</button>
						</div>

						<div class="btn-group visible-xs clearfix">
							<button type="button" class="btn btn-primary btn-state-toggle" data-method="moveTo" data-option="0" data-toggle="tooltip" title="移动到原点">
					          <span class="docs-tooltip">
					            <span class="fa fa-arrows-alt"></span>
					          </span>
							</button>
						</div>

						<div class="btn-group clearfix">
							<button type="button" class="btn btn-primary btn-state-toggle" data-method="rotate" data-option="-45" data-toggle="tooltip" title="左旋转45度">
				            <span class="docs-tooltip">
				              <span class="fa fa-rotate-left"></span>
				            </span>
							</button>
							<button type="button" class="btn btn-primary btn-state-toggle" data-method="rotate" data-option="45" data-toggle="tooltip" title="右旋转45度">
				            <span class="docs-tooltip">
				              <span class="fa fa-rotate-right"></span>
				            </span>
							</button>
						</div>

						<div class="btn-group visible-xs clearfix">
							<button type="button" class="btn btn-primary btn-state-toggle" data-method="move" data-option="-10" data-second-option="0" data-toggle="tooltip" title="图片左移">
				            <span class="docs-tooltip">
				              <span class="fa fa-arrow-left"></span>
				            </span>
							</button>
							<button type="button" class="btn btn-primary btn-state-toggle" data-method="move" data-option="10" data-second-option="0" data-toggle="tooltip" title="图片右移">
				            <span class="docs-tooltip">
				              <span class="fa fa-arrow-right"></span>
				            </span>
							</button>
							<button type="button" class="btn btn-primary btn-state-toggle" data-method="move" data-option="0" data-second-option="-10" data-toggle="tooltip" title="图片上移">
				            <span class="docs-tooltip">
				              <span class="fa fa-arrow-up"></span>
				            </span>
							</button>
							<button type="button" class="btn btn-primary btn-state-toggle" data-method="move" data-option="0" data-second-option="10" data-toggle="tooltip" title="图片下移">
				            <span class="docs-tooltip">
				              <span class="fa fa-arrow-down"></span>
				            </span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer" style="padding:10px;">
			<button type="button" class="btn btn-default" data-dismiss="modal" onclick="cropperWnd.closeCropperDialog('')" >关闭</button>
			<a class="btn btn-primary btn-state-toggle btn-crop-upload">剪裁并上传</a>
		</div>
	</div>
</div>
<!-- Scripts -->
<script src="${envData.staticRoot}/js/utils/upload/js/jquery-2.2.2.min.js"></script>
<script src="${envData.staticRoot}/js/utils/upload/js/bootstrap.min.js"></script>
<script src="${envData.staticRoot}/js/utils/upload/js/cropper.min.js"></script>
<script src="${envData.staticRoot}/js/utils/upload/js/messenger.js"></script>
<script src="${envData.staticRoot}/js/utils/loading.js"></script>
<script>
	var CropperWnd = function () {
        this.s_domain = "unknown";
        this.s_aspectRatio = "${map.ratio_w!'1'}:${map.ratio_h!'1'}";
        this.s_thumbnail;
        this.i_w = ${map.ratio_w!'1'};
        this.i_h = ${map.ratio_h!'1'};
        this.messager = new Messenger('dynamic_creation_upload_iframe', 'UploadCropperMessenger');
        this.messager.addTarget(window.parent, 'parentWindow');
        this.sendMessage=function (sMsg) {
            this.messager.targets['parentWindow'].send(sMsg);
        }
        this.console = window.console || { log: function () {} };
        this.container = document.querySelector('.img-container');
        this.image = this.container.getElementsByTagName('img').item(0);
        this.download = document.getElementById('download');
        this.actions = document.getElementById('actions');
        this.dataX = document.getElementById('dataX');
        this.dataY = document.getElementById('dataY');
        this.dataHeight = document.getElementById('dataHeight');
        this.dataWidth = document.getElementById('dataWidth');
        this.dataRotate = document.getElementById('dataRotate');
        this.options = {
            aspectRatio: this.i_w / this.i_h,
            preview: '.img-preview',
            build: function () {
                console.log('build');
            },
            built: function () {
                console.log('built');
            },
            cropstart: function (e) {
                console.log('cropstart', e.detail.action);
            },
            cropmove: function (e) {
                console.log('cropmove', e.detail.action);
            },
            cropend: function (e) {
                console.log('cropend', e.detail.action);
            },
            crop: function (e) {
                var data = e.detail;
                $.fn.CropperWnd.dataX.value = Math.round(data.x);
                $.fn.CropperWnd.dataY.value = Math.round(data.y);
                $.fn.CropperWnd.dataHeight.value = Math.round(data.height);
                $.fn.CropperWnd.dataWidth.value = Math.round(data.width);
                $.fn.CropperWnd.dataRotate.value = !$.fn.CropperWnd.isUndefined(data.rotate) ? data.rotate : '';
            },
            zoom: function (e) {
                $.fn.CropperWnd.console.log('zoom', e.detail.ratio);
            }
        };
        this.cropper = new Cropper(this.image, this.options);
        this.isUndefined=function (obj) {
            return typeof obj === 'undefined';
        }
        this.preventDefault=function (e) {
            if (e) {
                if (e.preventDefault) {
                    e.preventDefault();
                } else {
                    e.returnValue = false;
                }
            }
        }
        this.upload=function (base64,typename){
            var msg = {imgBody:base64,imgType:typename};
            $.fn.CropperWnd.sendMessage(JSON.stringify(msg));
        }
        this.refreshAspectRatio=function (s_ar){
            if (s_ar){
                var s_w_h = s_ar.split(":");
                if (s_w_h && s_w_h.length==2){
                    $(".docs-toggles").empty();
                    this.i_w = parseInt(s_w_h[0]);
                    this.i_h = parseInt(s_w_h[1]);
                    var shtml = '<label class="btn">宽高比例： </label>';
                    shtml += '<label class="btn btn-primary" data-toggle="tooltip" title="宽高比: '+this.i_w+' / '+this.i_h+'">';
                    shtml += '<input type="radio" class="sr-only" id="aspectRatio1" name="aspectRatio" value="'+(this.i_w/(this.i_h*1.0)).toFixed(2)+'">';
                    shtml += '<span class="docs-tooltip">'+s_ar+'</span>';
                    shtml += '</label>';
                    $(".docs-toggles").html(shtml);
                    this.options.aspectRatio = this.i_w / this.i_h;
                }
            }
            if (this.cropper)
                this.cropper.destroy();
            this.cropper = new Cropper(this.image, this.options);
        }
        this.closeCropperDialog=function (data){
            this.sendMessage(data);
        }
        $.fn.CropperWnd=this;
    }
    var cropperWnd = new CropperWnd();

    $(function(){
        $(".img-container").on("click",function(){
            inputImage.click();
        })
        // Tooltip
        $(".btn-state-toggle").attr("disabled", true);
        // Options
        cropperWnd.actions.querySelector('.docs-toggles').onclick = function (event) {
            var e = event || window.event;
            var target = e.target || e.srcElement;
            var cropBoxData;
            var canvasData;
            var isCheckbox;
            var isRadio;
            if (!cropperWnd.cropper) {
                return;
            }
            if (target.tagName.toLowerCase() === 'span') {
                target = target.parentNode;
            }
            if (target.tagName.toLowerCase() === 'label') {
                target = target.getElementsByTagName('input').item(0);
            }
            isCheckbox = target.type === 'checkbox';
            isRadio = target.type === 'radio';
            if (isCheckbox || isRadio) {
                if (isCheckbox) {
                    cropperWnd.options[target.name] = target.checked;
                    cropBoxData = cropper.getCropBoxData();
                    canvasData = cropper.getCanvasData();
                    cropperWnd.options.built = function () {
                        console.log('built');
                        cropperWnd.cropper.setCropBoxData(cropBoxData).setCanvasData(canvasData);
                    };
                } else {
                    cropperWnd.options[target.name] = target.value;
                    cropperWnd.options.built = function () {
                        console.log('built');
                    };
                }
                //Restart
                cropperWnd.cropper.destroy();
                cropperWnd.cropper = new Cropper(image, options);
                $(".btn-state-toggle").attr("disabled", true);
            }
        };

        // Methods
        cropperWnd.actions.querySelector('.docs-buttons').onclick = function (event) {
            var e = event || window.event;
            var target = e.target || e.srcElement;
            var result;
            var input;
            var data;
            if (!cropperWnd.cropper) {
                return;
            }
            while (target !== this) {
                if (target.getAttribute('data-method')) {
                    break;
                }
                target = target.parentNode;
            }
            if (target === this || target.disabled || target.className.indexOf('disabled') > -1) {
                return;
            }
            data = {
                method: target.getAttribute('data-method'),
                target: target.getAttribute('data-target'),
                option: target.getAttribute('data-option'),
                secondOption: target.getAttribute('data-second-option')
            };
            if (data.method) {
                if (typeof data.target !== 'undefined') {
                    input = document.querySelector(data.target);
                    if (!target.hasAttribute('data-option') && data.target && input) {
                        try {
                            data.option = JSON.parse(input.value);
                        } catch (e) {
                            cropperWnd.console.log(e.message);
                        }
                    }
                }
                if (data.method === 'getCroppedCanvas') {
                    data.option = JSON.parse(data.option);
                }
                result = cropperWnd.cropper[data.method](data.option, data.secondOption);
                switch (data.method) {
                    case 'getCroppedCanvas':
                        if (result) {
                            var imgType = 'image/jpeg';
                            var base64 = result.toDataURL(imgType);
                            console.log( JSON.stringify(result) );
                            // Bootstrap's Modal
                            cropperWnd.upload(base64,imgType);
                        }
                        break;
                    case 'reset':
                        cropperWnd.cropper.destroy();
                        cropperWnd.cropper = new Cropper(cropperWnd.image, cropperWnd.options);
                        $(".img-container").on("click",function(){
                            inputImage.click();
                        })
                        break;
                    case 'destroy':
                        cropperWnd.cropper = null;
                        break;
                }
                if (typeof result === 'object' && result !== cropperWnd.cropper && input) {
                    try {
                        input.value = JSON.stringify(result);
                    } catch (e) {
                        cropperWnd.console.log(e.message);
                    }
                }
            }
        };

        document.body.onkeydown = function (event) {
            var e = event || window.event;
            if (!cropperWnd.cropper || this.scrollTop > 300) {
                return;
            }
            switch (e.charCode || e.keyCode) {
                case 37:
                    cropperWnd.preventDefault(e);
                    cropperWnd.cropper.move(-1, 0);
                    break;
                case 38:
                    cropperWnd.preventDefault(e);
                    cropperWnd.cropper.move(0, -1);
                    break;
                case 39:
                    cropperWnd.preventDefault(e);
                    cropperWnd.cropper.move(1, 0);
                    break;
                case 40:
                    cropperWnd.preventDefault(e);
                    cropperWnd.cropper.move(0, 1);
                    break;
            }
        };
        // Import image
        var inputImage = document.getElementById('inputImage');
        var URL = window.URL || window.webkitURL;
        var blobURL;
        if (URL) {
            inputImage.onchange = function(){
                var files = this.files;
                var file;
                if (cropperWnd.cropper && files && files.length) {
                    file = files[0];
                    if (/^image\/\w+/.test(file.type)) {
                        blobURL = URL.createObjectURL(file);
                        cropperWnd.cropper.reset().replace(blobURL);
                        cropperWnd.cropper.moveTo(0, 0);
                        inputImage.value = null;
                        $(".img-container").off("click");
                        $(".btn-state-toggle").attr("disabled", false);
                    } else {
                        window.alert('Please choose an image file.');
                    }
                }
            };
        } else {
            inputImage.disabled = true;
            inputImage.parentNode.className += ' disabled';
        }

        $(".btn-crop-upload").on("click",function(){
            if ($(this).attr("disabled")){
                return ;
            }
            var result;
            var data;
            if (!cropperWnd.cropper) {
                return;
            }
            loading.show();
            result = cropperWnd.cropper.getCroppedCanvas();
            if (result===undefined){
                loading.hide();
                alert("请选择要上传的图片");
                return ;
            }
            var imgType = 'image/jpeg';
            var base64 = result.toDataURL(imgType);
            cropperWnd.upload(base64,imgType);
        })
    })
</script>
</body>
</html>
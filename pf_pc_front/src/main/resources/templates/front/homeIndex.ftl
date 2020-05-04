
<html>
 <head>
    <#include "./taglib.ftl">
 </head>
 <body>
   <!-- Small boxes (Stat box) -->
   <section class="content">
      <div class="row">
        <div class="col-lg-3 col-xs-3">
          <!-- small box -->
          <div class="small-box bg-aqua">
            <div class="inner">
              <h3>${order6!'0'}</h3>
              <p>申请退款中的订单</p>
            </div>
            <div class="icon" style="padding-top: 10px;">
              <i class="ion ion-bag"></i>
            </div>
            <a href="#" class="small-box-footer">去处理 <i class="fa fa-arrow-circle-right"></i></a>
          </div>
        </div><!-- ./col -->
        <div class="col-lg-3 col-xs-3">
          <!-- small box -->
          <div class="small-box bg-green">
            <div class="inner">
              <h3>${order7!'0'}</sup></h3>
              <p>待确认退款的订单</p>
            </div>
            <div class="icon" style="padding-top: 10px;">
              <i class="ion ion-ios-cart-outline"></i>
            </div>
            <a href="#" class="small-box-footer">去处理  <i class="fa fa-arrow-circle-right"></i></a>
          </div>
        </div><!-- ./col -->
        <div class="col-lg-3 col-xs-3">
          <!-- small box -->
          <div class="small-box bg-yellow">
            <div class="inner">
              <h3>${mechant0!'0'}</h3>
              <p>本月新增商户数量</p>
            </div>
            <div class="icon" style="padding-top: 15px;">
              <i class="fa fa-google-plus"></i>
            </div>
            <a href="#" class="small-box-footer"><i class="fa fa-odnoklassniki"></i></a>
          </div>
        </div><!-- ./col -->
        <div class="col-lg-3 col-xs-3">
          <!-- small box -->
          <div class="small-box bg-red">
            <div class="inner">
              <h3>${endUser0!'0'}</h3>
              <p>本月新增用户数量</p>
            </div>
            <div class="icon">
              <i class="ion ion-person-add"></i>
            </div>
            <a href="#" class="small-box-footer"><i class="fa fa-odnoklassniki"></i></a>
          </div>
        </div><!-- ./col -->
      </div><!-- /.row -->
      
      <div class="row">
        <div class="col-md-12">
          <div class="box">
            <div class="box-header with-border">
              <h3 class="box-title"></h3>
            </div><!-- /.box-header -->
            <div class="box-body">
              <div class="row">
                <div class="col-md-4">
              <!-- Info Boxes Style 2 -->
              <div class="info-box bg-yellow">
                <span class="info-box-icon"><i class="ion ion-ios-people-outline"></i></span>
                <div class="info-box-content">
                  <span class="info-box-text">用户总数</span>
                  <span class="info-box-number">${endUser1!'0'}</span>
                </div><!-- /.info-box-content -->
              </div><!-- /.info-box -->
              <div class="info-box bg-green">
                <span class="info-box-icon"><i class="fa fa-flag-o"></i></span>
                <div class="info-box-content">
                  <span class="info-box-text">商家总数</span>
                  <span class="info-box-number">${mechant1!'0'}</span>
                </div><!-- /.info-box-content -->
              </div><!-- /.info-box -->
       
            </div>
                <div class="col-md-4">
                 
                </div><!-- /.col -->
              </div><!-- /.row -->
            </div><!-- ./box-body -->
          </div><!-- /.box -->
        </div><!-- /.col -->
      </div><!-- /.row --></section>
 </body>


 </html>
#!/bin/bash
APP_Name="pf_pc_front"
JAVA_DIR="/home/java/jre1.8.0_321/bin"
INSTALL_DIR="/var/local/pf_pcfront"
Das_ID=$(ps -ef |grep $APP_Name |grep -v "grep"| awk '{print $2}')
Das_Id=$( ps -ef |grep $APP_Name |grep -v "grep"| awk '{print $2}' | wc -l)
if [ $Das_Id -eq 0 ];then
echo ""
echo "application $APP_Name is not run..."
fi

if [ $Das_Id -ne 0 ];then
echo "application $APP_Name=$Das_ID"
echo "application $APP_Name=$Das_ID ready to close"
kill -9 $Das_ID
echo "application $APP_Name=$Das_ID exec close"
sleep 2
Das_Id_a=$( ps -ef |grep $APP_Name |grep -v "grep"| awk '{print $2}' | wc -l)
if [ $Das_Id_a -eq 0 ];then
echo "application $APP_Name is closed success"
fi
if [ $Das_Id_a -ne 0 ];then
echo "application $APP_Name close failed，shell exit..."
exit
fi
fi

echo "==============================="
echo "ready start application $APP_Name"
cd $INSTALL_DIR
nohup $JAVA_DIR/java -Dloader.path=./lib,./bin -jar ./$APP_Name-1.0.1-classes.jar >out.log 2>&1 &
echo "application $APP_Name exec start"
sleep 1
Das_Id_b=$( ps -ef |grep $APP_Name |grep -v "grep"| awk '{print $2}' | wc -l)
if [ $Das_Id_b -eq 0 ];then
echo "application $APP_Name start failed"
exit
fi
if [ $Das_Id_b -ne 0 ];then
echo "application $APP_Name start success!"
fi
exit
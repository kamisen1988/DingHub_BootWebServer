
var ddtcOperlock = {
    //websdk2bURL 是丁丁停车为合作伙伴（B端）提供的SDK服务接口
    //和ddtcDingHub相关的默认地址为"http://182.92.99.168:18082/ddtcDingHub/"
    //websdk2bURL : "http://localhost:18082/ddtcDingHub/"
    websdk2bURL : "http://182.92.99.168:18082/ddtcDingHub/"
}

function htmlRequest(url) {
    var date = new Date();
    var hubMac = document.getElementById('hubMac').value;
    var divMsg = document.getElementById("errMessageTxt");
    if(hubMac==""||hubMac==null)
    {
        divMsg.innerHTML += '<div style="color:red">' + util.getFormatDate(date) + ' Error> : ' + '请输入HubMac' + '</div>'
    }
    else if(hubMac.length!=14 || hubMac.indexOf('DZ')==-1)
    {
        divMsg.innerHTML += '<div style="color:red">' + util.getFormatDate(date) + ' Error> : ' + 'HubMac格式有误' + '</div>'
    }
    else{
        //先把url中的参数（不含toBToken）截出来发送到交互框上
        var urlCommand = url.substring(url.lastIndexOf('/')+1, url.indexOf('toBtoken')-1)
        divMsg.innerHTML += '<div style="color:green">' + util.getFormatDate(date) + ' Client> : ' + urlCommand + '</div>'

        //再把url结果发送到交互框上
        $.ajax({
            type: 'GET',
            url: url,
            success: function (data) {
                divMsg.innerHTML += util.getFormatDate(date) + ' Reply> : ' + data.data + data.errMessage + '</div>'
            },
            error: function (data) {
                divMsg.innerHTML += '<div style="color:red">' + util.getFormatDate(date)
                    + ' Error> : ' + data.data + data.errMessage + '</div>'
            }
        })
    }
}

function sendNUSOper(hubMac,lockMac,toBtoken,operation) {
    var url =  ddtcOperlock.websdk2bURL + "operNUSLock?hubMac="+hubMac+"&lockMac="+lockMac+"&operType="+operation+"&toBtoken="+toBtoken;
    htmlRequest(url)
}
function findHubMacInfo(hubMac,toBtoken,oper) {
    var url =  ddtcOperlock.websdk2bURL + "operNUSLockHubMac?hubMac="+hubMac+"&oper=search"+"&toBtoken="+toBtoken;
    htmlRequest(url)
}
function getMessage(hubMac,toBtoken) {
    var url =  ddtcOperlock.websdk2bURL + "getMessage?hubMac="+hubMac+"&toBtoken="+toBtoken;
    htmlRequest(url)
}
function setTimer(timer,hubMac,toBtoken) {
    var url =  ddtcOperlock.websdk2bURL + "setTimer?timer="+timer+"&hubMac="+hubMac+"&toBtoken="+toBtoken;
    htmlRequest(url)
}
function hubOper(hubMac,operation,toBtoken) {
    var url =  ddtcOperlock.websdk2bURL + "hubOper?hubMac=" + hubMac + "&operation=" + operation + "&toBtoken=" + toBtoken;
    htmlRequest(url)
}
function hubSend433(hubMac,cmd,operType,toBtoken) {
    var url =  ddtcOperlock.websdk2bURL + "hubSend433?hubMac="+hubMac+"&cmd="+cmd+"&operType="+operType+"&toBtoken="+toBtoken;
    htmlRequest(url)
}

function judgementForOffPowerAndSleep(hubMac,operation,break_time,toBtoken) {
    var date = new Date();
    var divMsg = document.getElementById("errMessageTxt");
    divMsg.innerHTML += '<div style="color:green">' + util.getFormatDate(date) + ' Client> : ' + operation + '</div>'
    if(break_time=='0') {
        hubOper(hubMac,operation,toBtoken)
    }
    else if(break_time==""||break_time==null) {
        divMsg.innerHTML += '<div style="color:red">' + util.getFormatDate(date) +
            ' Error> : ' + '请输入6位唤醒时间,若不需要唤醒,请输入0' + '</div>'
    }
    else if(operation.indexOf("HUB_WT")>=0&&operation.length!==12) {
        divMsg.innerHTML += '<div style="color:red">' + util.getFormatDate(date) +
            ' Error> : ' + '唤醒时间需要满6位数字，例如000030' + '</div>'
    }else{
        hubOper(hubMac,operation,toBtoken)
    }
}

//TODO 设置hub名称成功后，新放一个“刷新hub列表”的html按键，调用getHubMacName函数，刷新hub list
function setHubMacName(hubMac,hubMacName,toBtoken) {
    var url =  ddtcOperlock.websdk2bURL + "setHubMacName?hubMac="+hubMac+"&hubMacName="+hubMacName+"&toBtoken="+toBtoken;
    htmlRequest(url);
    getHubMacName();
}

function refreshHubMacName(hubMac,hubMacName,toBtoken) {
    getHubMacName();
}

//开启指定hub的noti，调用下面函数，带token发给websdk2b server
function notiHub(hubMac){
    var url =  ddtcOperlock.websdk2bURL + "notiHub?hubMac="+hubMac +"&toBtoken="+toBtoken;
    htmlRequest(url);
}
//初始化hubmacName列表
function getHubMacName() {
    var date = new Date();
    var divMsg = document.getElementById("errMessageTxt");
    $.ajax({
        type : "get",
        url : ddtcOperlock.websdk2bURL + "getHubMacName?toBtoken="+document.getElementById('toBtoken').value,
        success : function(data, status) {
            console.log(data.data)
            var temp = data.data.substring(0,data.data.length-1)
            var arr = temp.split(",")
            console.log(arr)
            if(arr==''){
                divMsg.innerHTML += '<div style="color:blue">' + util.getFormatDate(date) + ' Server> : ' + 'Hub列表为空' + '</div>'
            }
            else{
                var i = 0
                //页面首次加载后，option置空，将第一个hub信息填入
                $("#select").empty();

                //将hub列表放入下拉框
                $.each(arr, function() {
                    $("#select").append(  //此处向select中循环绑定数据
                        "<option name="+i+">" + arr[i++]+ "</option>");
                });

                //将第一个hub信息填入
                var options=$("#select option:selected")
                var t = options.text()
                var i = t.indexOf(":")
                var j = t.lastIndexOf(":")          //为了适配M26_L3:DZD82C7D572F1A:off这样传回的格式
                $("#hubMacName").val(t.substring(0,i))
                $("#hubMac").val(t.substring(i+1,j))

                //下拉列表改变后的逻辑
                $("#select").on("change",function () {
                    var options=$("#select option:selected")
                    console.log(options.text())
                    var t = options.text()
                    var i = t.indexOf(":")
                    var j = t.lastIndexOf(":")
                    $("#hubMacName").val(t.substring(0,i))
                    $("#hubMac").val(t.substring(i+1,j))
                })
                divMsg.innerHTML += '<div style="color:blue">' + util.getFormatDate(date) + ' Server> : ' + 'Hub列表加载完毕' + '</div>'
            }
        },
        error: function (data) {
            divMsg.innerHTML += '<div style="color:red">' + util.getFormatDate(date)
                + ' Error> : ' + data.data + data.errMessage + '</div>'
        }
    });
}

//获取当前用户的推送URL
function getUrl(){
    $.ajax({
        type: "get",
        url: ddtcOperlock.websdk2bURL + "getUrl?toBtoken=" + document.getElementById('toBtoken').value,
        success: function (data) {
            if(data.errNo == 200){
                $("#url").val(data.data)
            }
            else{
                console.log(data.errMessage)
            }
        }
    });
}

//设置当前用户的推送URL
function setUrl(URL) {
    $.ajax({
        type: "get",
        url: ddtcOperlock.websdk2bURL + "getUrl?toBtoken=" + document.getElementById('toBtoken').value,
        success: function (data) {
            if(data.data == "" || URL != data.data){
                if(!data.data) {
                    if (URL != ''){
                        if(URL.substr(0, 4) != "http" || URL.substr(-1, 1) == "/"){
                            alert('URL 的格式不正确！');
                        }
                        else{
                            if (confirm('当前URL为空，确定要执行新增操作吗?')) {
                                $.ajax({
                                    type: "get",
                                    url: ddtcOperlock.websdk2bURL + "saveUrl?toBtoken=" + document.getElementById('toBtoken').value + '&amp;url=' + URL,
                                    success: function (data) {
                                        if(data.errNo == 200){
                                            alert("新增成功！")
                                        }
                                        else{
                                            alert(data.errMessage)
                                        }
                                        return true;
                                    }
                                });
                            }
                            else {
                                return false;
                            }
                        }
                    }
                    else {
                        alert("当前URL为空，请重新设置URL！")
                    }
                }
                else {
                    if(!URL){
                        if (confirm('输入的URL为空，确定要执行清空操作吗?')) {
                            $.ajax({
                                type: "get",
                                url: ddtcOperlock.websdk2bURL + "deleteUrl?toBtoken=" + document.getElementById('toBtoken').value,
                                success: function (data) {
                                    $("#url").val("")
                                    if(data.errNo == 200){
                                        alert("删除成功！")
                                    }
                                    else{
                                        alert(data.errMessage)
                                    }
                                }
                            });
                        }
                        else {
                            return false;
                        }
                    }
                    else{
                        if(URL.substr(0, 4) != "http" || URL.substr(-1, 1) == "/"){
                            alert('URL 的格式不正确！');
                        }
                        else{
                            if (confirm('当前URL不为空，确定要执行更新操作吗?')) {
                                $.ajax({
                                    type: "get",
                                    url: ddtcOperlock.websdk2bURL + "updateUrl?toBtoken=" + document.getElementById('toBtoken').value + '&amp;url=' + URL,
                                    success: function (data) {
                                        if(data.errNo == 200){
                                            alert("更新成功！")
                                        }
                                        else{
                                            alert(data.errMessage)
                                        }
                                    }
                                });
                            }
                            else {
                                return false;
                            }
                        }
                    }

                }
            }
            else{
                alert("该URL已存在！")
            }
        }
    })
}


function login() {
    var username = $("#username").val();
    var password = $("#password").val();
    var checkCode = $("#code").val();
    if (judgeParam(username,password,checkCode)) {
        $.ajax({
            beforeSend:function () {
                index =  layer.load();
            },
            complete:function () {
                layer.close(index);
            },
            url:"./login.do",
            type:"post",
            dataType:"json",
            data:{"username":username,"password":password,"checkNum":checkCode},
            success:function (result) {
                if (result.code === 1){
                    window.location = "./index.html";
                }else {
                    layer.msg(result.message,{icon:5});
                }
            },
            error:function (result) {
                layer.msg("内部错误或网络不佳请稍后重试",{icon: 5});
            }
        });
    }else {
        layer.msg("请填写完以上信息",{icon:5});
    }

}

//判断空参 无空 返回true 否则返回false
function judgeParam() {
    for (var i = 0; i < arguments.length; ++i) {
        var param = arguments[i].trim();
        if (param == null || param.length === 0) {
            return false;
        }
    }
    return true;
}
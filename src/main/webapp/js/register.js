function register() {
    var username = $("#username").val();
    var password = $("#inputPassword3").val();
    var email = $("#inputEmail3").val();
    var checkNum = $("#checkNum").val();
    if (!checkPassword()) {
        layer.msg("两次密码不一致呦",{icon:5});
        return ;
    }
    if (judgeParam(username, password, email, checkNum)) {
        $.ajax({
            beforeSend:function () {
                index =  layer.load();
            },
            complete:function () {
                layer.close(index);
            },
            url: "./register.do",
            type: "post",
            dataType: "json",
            data:{"username":username,"password":password,"email":email,"checkNum":checkNum},
            success:function (result) {
                if (result.code === 1) {
                    layer.msg("恭喜注册成功,点击返回登录",{icon:6})
                }else {
                    layer.msg(result.message,{icon:5});
                }
            }
        });
    } else {
        layer.msg(" 请填写完以上信息", {icon: 5});
    }
}

function toLoginPage() {
    window.location = "./login.html";
}

//判断空参 无空 返回true 否则返回false
function judgeParam() {
    for (var i = 0; i < arguments.length; ++i) {
        console.log(arguments[i]);
        var param = arguments[i].trim();
        if (param == null || param.length === 0) {
            return false;
        }
    }
    return true;
}

function checkPassword() {
    var password = $("#inputPassword3").val();
    var confirmpwd = $("#confirmpwd").val();
    if (confirmpwd === password) {
        return true;
    }else {
        return false;
    }
}
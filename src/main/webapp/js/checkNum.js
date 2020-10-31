function randomRgbColor() { //随机生成RGB颜色
    var r = Math.floor(Math.random() * 256); //随机生成256以内r值
    var g = Math.floor(Math.random() * 256); //随机生成256以内g值
    var b = Math.floor(Math.random() * 256); //随机生成256以内b值
    return "rgb(" + r + "," + g + "," + b + ")"; //返回rgb(r,g,b)格式颜色
}
window.onload = function () {
    getCheckNum();
    document.getElementById("changeImg").onclick=function(){
        getCheckNum();
    }
}

function draw(checkNum){
    var canvas = document.getElementById("canvas");
    var context = canvas.getContext("2d");
    canvas.width = 120;
    canvas.height = 40;
    context.strokeRect(0, 0, 120, 40);
    for (var i = 0; i <= 3; i++) {
        var x = 20 + i * 20;
        var y = 20 + Math.random() * 10;
        var j = Math.floor(Math.random() * 30);
        var deg = Math.random() * 90 * Math.PI / 180;//随机弧度
        context.fillStyle = randomRgbColor();
        context.font = "bold 20px 微软雅黑";
        //修改坐标原点和旋转角度
        context.translate(x, y);
        context.rotate(deg);
        context.fillText(checkNum.charAt(i), 0, 0);
        //恢复坐标原点和旋转角度
        context.rotate(-deg);
        context.translate(-x, -y);
    }
    //干扰线
    for (var i = 0; i < 8; i++) {
        context.strokeStyle = randomRgbColor();
        context.beginPath();
        context.moveTo(Math.random() * 120, Math.random() * 40);
        context.lineTo(Math.random() * 120, Math.random() * 40);
        context.stroke();
    }
    /**绘制干扰点**/
    for (var i = 0; i < 20; i++) {
        context.fillStyle = randomRgbColor();
        context.beginPath();
        context.arc(Math.random() * 120, Math.random() * 40, 1, 0, 2 * Math.PI);
        context.fill();
    }
}

function getCheckNum() {
    $.ajax({
        url:"./getCheckNum.do",
        type:"post",
        dataType:"json",
        success:function (result) {
            if (result.code === 1) {
                draw(result.obj);
            }else {
                alert("获取验证码失败");
            }
        },
        error:function () {
            alert("内部错误");
        }
    });
}
var initFlag = true;
var curPage = 1;
$(function () {
    // 1.tab切换效果
    $(".sort ul li").eq(0).addClass("active").siblings().removeClass("active");
    $(".list .items").eq(0).stop().show().siblings(".items").stop().hide();
    //初始化 退出按钮
    var loginFlag = $.cookie("login");
    if (loginFlag == null) {
        $(".quit").html("登录");
    } else {
        $(".quit").html("退出")
    }
    $(".sort ul li").click(function () {
        // 1.1获取当前菜单的索引
        var idx = $(this).index();
        $(this).addClass("active").siblings("").removeClass("active");
        $(".list .items").eq(idx).stop().show().siblings(".items").stop().hide();

        var title = $(this).text();
        switch (title) {
            case "配件":
                getProList("mountings.do");
                break;
            case "服饰":
                getProList("costume.do");
                break;
            case "家用":
                getProList("household.do");
                break;
            default:
                getProList("allCommodity.do");
                break;
        }

    })

// 2.购物车列表显示/隐藏
    $(".gwc_icon").click(function () {
        $(".shoppingCart").stop().show();
        $(".mask").stop().show();

        getShopList();

    })

// 3.遮罩隐藏
    $(".mask").click(function () {
        $(".shoppingCart").stop().hide();
        $(this).stop().hide();
    })

    $(".del,.submit").click(function () {
        $(".shoppingCart").stop().hide();
        $(".mask").stop().hide();
    })


// 3.产品列表数据渲染
    getProList("allCommodity.do");
//4.添加购物车
    $(".list .items").on("click", "li", function () {

        var type = $(this).attr("type")
        var id = $(this).attr("id");
        addShopCart(type, id);
    })

// 6.添加购物车方法
    function addShopCart(type, id) {

        $.ajax({

            beforeSend: function () {
                index = layer.load();
            },
            complete: function () {
                layer.close(index);
            },
            url: "shopping.do?msg=add&commodity_type=" + type + "&commodity_id=" + id,
            dataType: "text",
            success: function (result) {
                if (result === "success") {
                    getShopList();
                }
            }
        })
    }

//5.购物车数据渲染
    function getShopList() {
        $.ajax({
            beforeSend: function () {
                index = layer.load();
            },
            complete: function () {
                layer.close(index);
            },
            url: "shopping.do?msg=select_all",
            dataType: "json",
            success: function (result) {
                var data = result.shoppings;
                var str = "";

                for (var i = 0; i < data.length; i++) {
                    str += `<li id="` + data[i].commodityId + `"type= "` + data[i].commodityType +
                        `" >
							<i class="iconfont icon-jianhao sub" ></i>
							<div class="content">
								<img src="` + data[i].picture +
                        `" />
								<div class="center">
									<p class="title">` + data[i].name + `</p>
									<p>价格:<i>` + data[i].price + `</i>元</p>
									<p>数量:<i class="num">` + data[i].num + `</i></p>
								</div>
								<div class="price">
									<span>小计：</span>
									<span class="xj">￥` +
                        (data[i].num * data[i].price).toFixed(2) + `</span>
								</div>
							</div>
						</li>`
                }
                $(".shoppingCart .pro_list ul").html(str);
                getSum();
            }

        })
    }


// 7.购物车单条数据删除操作
    $(".shoppingCart .pro_list").on("click", ".sub", function () {

        var id = $(this).parent().attr("id");
        var type = $(this).parent().attr("type");

        $.ajax({
            url: "shopping.do?msg=delete&commodity_type=" + type + "&commodity_id=" + id,
            //有坑 dataType 和服务器数据类型不一致时 会执行error语句 而不是success
            dataType: "text",
            success: function (result) {
                getShopList()
            }
        });
    })


//8.清空购物车
    $(".shoppingCart .del").click(function () {
        removeShopCart();
    })

    function removeShopCart() {
        $.ajax({
            url: "shopping.do?msg=delete_all",
            dataType: "text",
            success: function (result) {
                if (!result) {
                    layer.msg("清空购物车成功！", {icon: 6})
                }
            }
        });
    }


// 提交订单
    /* 	$(".shoppingCart .submit").click(function() {
            $.ajax({
                url: "order?msg=commit",
                dataType: "json",
                success: function(result) {
                    if (result=="success") {
                        removeShopCart();
                        alert("提交成功")
                    }
                }

            })
        }) */


// 9. 计算总计和总额模块

    function getSum() {
        var count = 0; // 计算总件数
        var money = 0; // 计算总价钱
        $(".pro_list .center .num").each(function (i, ele) {
            count += parseInt($(ele).text());
        });
        $(".pro_list .price .xj ").each(function (i, ele) {
            money += parseFloat($(ele).text().substr(1));
        });

        $(".shoppingCart .title span i").text(count);
        $(".total .sum").text("￥" + money.toFixed(2));

    }


})


function getProList(flag) {
    $.ajax({
        beforeSend: function () {
            index = layer.load();
        },
        complete: function () {
            layer.close(index);
        },
        url: flag,
        dataType: "json",
        success: function (result) {
            var data;

            var flagSplit = flag.split("?");
            flag = flag.split("?")[0];

            // = 1 为第1次请求 要初始化 分页条
            if (flagSplit.length === 1) {
                initFlag = true;
            }
            switch (flag) {
                case "mountings.do":
                    data = result.mountingsData;
                    break;
                case "costume.do":
                    data = result.costumeData;
                    break;
                case "household.do":
                    data = result.householdData;
                    break;
                default:
                    data = result.allCommodity;
                    break;
            }


            var str = "";
            for (var i = 0; i < data.length; i++) {
                var commodityName = data[i].name;
                if (commodityName.length > 8) {
                    var s = commodityName.substr(0,8);
                    commodityName =s + "...";
                }
                console.log(commodityName);
                str += `<li id="` + data[i].id + `"type= "` + data[i].commodityType + `">
							<i class="iconfont icon-ic_jiarugouwuche  gwc"></i>
							<img src="` + data[i].picture + `" alt="">
							<p>` + commodityName + `</p>
							<span>￥<i>` + data[i].price + `</i></span>
						</li>`
            }
            if (initFlag) {
                curPage = 1;
                initPaging(result.size, flag);
                initFlag = false;
            }
            $(".list .items ul").html(str);
        }
    });
}


function initPaging(allCommoditySize, urlFlag) {
    var size = Math.ceil(allCommoditySize / 10);
    //初始化列表
    initBar(allCommoditySize);
    //为a标签绑定时间
    $("#paging").on("click", "a", function () {
        if ($(this).attr("id") !== "next" && $(this).attr("id") !== "pre") {
            var clickNum = $(this).html();
            $(".current").removeClass("current");
            $(this).addClass("current");
            curPage = clickNum;
            //检测 pre 与 next 是否可点
            checkForPreAndNext(curPage, size);
            getProList(urlFlag + "?curPage=" + clickNum);
        } else if ($(this).attr("id") === "next") {
            $("#a" + curPage).removeClass("current");
            if (parseInt(curPage) + 1 < size) {
                curPage++;
            } else {
                curPage = size;
            }
            $("#a" + curPage).addClass("current");
            checkForPreAndNext(curPage, size);
            getProList(urlFlag + "?curPage=" + curPage);
        } else {
            $("#a" + curPage).removeClass("current");
            if (parseInt(curPage) - 1 > 0) {
                curPage--;
            } else {
                curPage = 1;
            }
            $("#a" + curPage).addClass("current");
            checkForPreAndNext(curPage, size);
            getProList(urlFlag + "?curPage=" + curPage);
        }
    });
}

//根据当前页改变状态
function checkForPreAndNext(curPage, allPage) {
    if (curPage === 1) {
        $("#pre").addClass("disabled");
    } else {
        $("#pre").removeClass("disabled");
    }

    if (curPage === allPage) {
        $("#next").addClass("disabled");
    } else {
        $("#next").removeClass("disabled");
    }
}

function initBar(allCommoditySize) {
    var size = Math.ceil(allCommoditySize / 10);
    var disabled = 1 === size ? "disabled" : "";
    var paging = "<li class=\"button\"><a class=\"disabled\" id='pre'>上一页</a></li>";
    if (size < 5) {
        for (var i = 1; i <= size; i++) {
            var classFlag = i === 1 ? "current" : "";

            paging += `
						<li><a class="` + classFlag + `" id="` + 'a' + i + `">` + i + `</a></li>
						`
        }
    } else {
        for (var i = 1; i <= 5; i++) {
            var classFlag = i === 1 ? "current" : "";
            paging += `
						<li><a class="` + classFlag + ` " id="` + 'a' + i + `">` + i + `</a></li>
					 `
        }
    }
    paging += '<li class="button"><a id="next" class="` + disabled  + ` ">下一页</a></li>';
    //写入界面
    $("#paging").html(paging);
}


//退出

function exit() {
    var quit = $(".quit").html();
    if (quit === "登录") {
        window.location="./login.html";
    }else {
        $.ajax({
            url:"./loginOut.do",
            dataType:"text",
            success:function (result){
                if (result === "success"){
                    window.location="./login.html";
                }
            }
        });
    }
}

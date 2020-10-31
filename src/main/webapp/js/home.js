$(function() {
	// 1.图片排列
	function getImgW() {
		var imgW = $(".pro_list li").width();
		var num = $(".pro_list ul li").length;
		var pro_listL = imgW * num / 2 + 200;

		$(".pro_list ul").width(pro_listL);
		$(".pro_list ul li:odd").css("padding-left", 40);
	}

	// 2.分类切换效果
	// 2.1 显示
	$(".header .left").click(function() {
		$(".sort").stop().slideDown();
		$(".sort ul li").eq(0).addClass("active").siblings().removeClass("active");
	})
	// 2.2 隐藏
	$(".quit").click(function() {
		$(".sort").stop().slideUp();
	})

    $(".sort ul").on("click","li",function(){
	// 2.3获取当前菜单的索引
		var idx = $(this).index();
		$(this).addClass("active").siblings("").removeClass("active");
		
		var title = $(this).text();
		switch (title) {
			case "配件":
				getProList("mountings");
				break;
			case "服饰":
				getProList("costume");
				break;
			case "家用":
				getProList("household");
				break;
			default:
				getProList("allCommodity");
				break;
		}
		$(".sort").stop().slideUp();
	})
	

	// 3.产品列表数据渲染
	getProList("allCommodity");
	function getProList(flag) {
		$.ajax({
			url: flag,
			dataType: "json",
			success: function(result) {
				var data;
				switch (flag) {
					case "mountings":
						data = result.mountingsData;
						break;
					case "costume":
						data = result.costumeData;
						break;
					case "household":
						data = result.householdData;
						break;
					default:
						data = result.allCommodity;
						break;
				}
				
				var str = "";
				for (var i=0;i<data.length;i++) {
					str+=`<li id="`+data[i].id+`"type= "`+data[i].type+`">
							<img src="`+data[i].picture+`" alt="">
							<p>`+data[i].name+`</p>
							<span>￥<i>`+data[i].price+`</i></span>
						</li>`
				}
					
				$(".pro_list ul").html(str);
				getImgW();
			}
		});
	}

// 4.购物车列表
	// 4.1 显示/隐藏
	$(".shoppingCart").click(function() {
		$(".shoppingList2").stop().slideDown();
		
		getShopList();//加载购物车数据
		getSum();
	})
	$(".shoppingList2 .title,.del,.submit").click(function() {
		$(".shoppingList2").stop().slideUp();
		
	})
	
	//4.2购物车数据渲染
	function getShopList() {
		 $.ajax({
			url: "shopping?msg=select_all",
			dataType: "json",
			success: function(result) {
				var data = result.shoppings;
				var str = "";
				console.log(data);
				for (var i=0;i<data.length;i++) {
					str+=`<li id="` + data[i].commodity_id + `"type= "` + data[i].type +
						`">
							<i class="iconfont icon-jianhao sub"></i>
							<div class="content">
								<img src="`+data[i].picture+`" />
								<div class="center">
								    <p class="title">`+data[i].name+`</p>
									<p>价格:<i>`+data[i].price+`</i>元</p>
									<p>数量:<i>`+data[i].num+`</i></p>
								</div>
								<div class="price">
									<span>小计</span>
									<span class="xj">￥`+(data[i].num*data[i].price).toFixed(2)+`</span>
								</div>
							</div>
						</li>`
				}
					
			    $(".pro_list2 ul").html(str);
				getSum();
				
			}
			
		}) 
	}


  //5.添加购物车
  // 5.1添加购物车
     $(".pro_list ul").on("click","li",function(){
		
		 var type = $(this).attr("type")
		 var id = $(this).attr("id");
		     addShopCart(type,id);

	})
   
   // 5.2添加购物车方法
      function addShopCart(type,id){

    		 $.ajax({
				 url:"shopping?msg=add&commodity_type="+type+"&commodity_id="+id,
    			 dataType:"json",
    			 success:function(result){
                  if(result=="success"){
                  		getShopList();
                  		alert("添加成功！")
                  }
    			 } 
    		 })
    	 }


	// 5.3 购物车单条数据删除操作
	$(".pro_list2 ul").on("click",".sub",function(){
		
		var id = $(this).parent().attr("id");
		var type = $(this).parent().attr("type");

		$.ajax({
			url: "shopping?msg=delete&commodity_type="+type+"&commodity_id="+id,
			dataType: "json",
			success: function(result) {
				getShopList()
				
			}
		});
		
	}); 


	//5.4 清空购物车
	$(".operation .del").click(function() {
		removeShopCart();
	})
	 function removeShopCart(){
		 $.ajax({
		 	url: "shopping?msg=delete_all",
		 	dataType: "json",
		 	success: function(result) {

		 	}
		 });
	 }


	// 6. 计算总计和总额模块
	function getSum() {
		var count = 0; // 计算总件数 
		var money = 0; // 计算总价钱
		$(".pro_list2 .center i ").each(function(i, ele) {
			count += parseInt($(ele).text());
		});
		$(".shoppingCart2 .title span i").text(count);

		$(".pro_list2 .price .xj ").each(function(i, ele) {
			money += parseFloat($(ele).text().substr(1));
		});

		$(".total .sum").text("￥" + money.toFixed(2));
	}
})

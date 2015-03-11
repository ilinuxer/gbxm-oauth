/**
 * Created by jimbo on 15-3-11.
 */
var gplus={

    oauth:function(){
        var app_name = $("#appName").val();
        var client_id = $("#clientId").val();
        var client_secret = $("#clientSecret").val();

        if(app_name==""||client_id==""||client_secret==""){
            alert("请输入完整的参数");
        }

        $.ajax({
            url:"/googleoauth",
            type:"post",
            data:{
                appName:app_name,
                clientId:client_id,
                clientSecret:client_secret
            },
            dataType:"text",
            success:function(data){

                //alert(JSON.stringify(data));
                //console.log(JSON.stringify(data))
                //window.location.href(data);
                window.open(data,"authorization","height=400,width=400");
                $("#appName").empty();
                $("#clientId").empty();
                $("#clientSecret").empty();
            },
            error:function(data){}
        })
    }
}

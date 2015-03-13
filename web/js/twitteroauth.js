/**
 * Created by jimbo on 15-3-12.
 */
var twitter={
    getUrl:function(){

        $.ajax({
            url:"/twitteroauth",
            type:"get",
            dataType:"text",
            success:function(data){
                window.open(data,"twitterOauth","height=400,width=700");
            },
            error:function(data){}
        })
    },

    commitPin:function(){
        var pin = $("#pin").val();
        $.ajax({
            url:"/twitteroauth",
            type:"post",
            data:{
                pin:pin
            },
            dataType:"text",
            success:function(data){
                alert(data);
            },
            error:function(data){
                alert(JSON.stringify(data));
            }

        })
    }


}
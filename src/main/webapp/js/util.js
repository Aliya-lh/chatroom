// 封装一个ajax函数，参数args为一个js对象
// 定义args对象属性如下：
// method: 请求方法
// url: 请求服务器资源的路径
// contentType: 请求正文的格式
// body: 请求正文
// callback: 回调函数，客户端接收到响应数据后，调用
function ajax(args){//var ajax = function(){}
    let xhr = new XMLHttpRequest();
    // 设置回调函数
    xhr.onreadystatechange = function(){
        // 4: 客户端接收到响应后回调
        if(xhr.readyState == 4){
            // 回调函数可能需要使用响应的内容，作为传入参数
            args.callback(xhr.status, xhr.responseText);
        }
    }
    xhr.open(args.method, args.url);
    //如果args中，contentType属性有内容，就设置Content-Type请求头
    if(args.contentType){//js中，if判断，除了判断boolean值，还可以判断字符串，对象等，有值就为true
        xhr.setRequestHeader("Content-Type", args.contentType);
    }
    //如果args中，设置了body请求正文，调用send(body)
    if(args.body){
        xhr.send(args.body);
    }else{//如果没有设置，调用send()
        xhr.send();
    }
}


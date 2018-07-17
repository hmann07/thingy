function getcook(){
var b = document.cookie.match('(^|;)\\s*' + "ME" + '\\s*=\\s*([^;]+)');
    return b ? b.pop() : '';
}

var m = 'localhost'
var ws = new WebSocket('ws://'+m+':9000/startWS')
  ws.onmessage = function( message ) { 
    $("#output").append("<p>" + message.data + "</p>")

    //console.log( message ); 
  };
ws.send('test')
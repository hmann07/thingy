function getcook(){
var b = document.cookie.match('(^|;)\\s*' + "ME" + '\\s*=\\s*([^;]+)');
    return b ? b.pop() : '';
}

var m = 'localhost'
var ws = new WebSocket('ws://'+m+':9000/startWS')
  ws.onmessage = function( message ) { 
  	var d = JSON.parse(message.data)
  	$(".progress-bar").width("" + (d.currentGeneration / d.totalGenerations) *100 + "%")
    $("#output").append("<p>generation " + d.currentGeneration + " finished</p>")

    //console.log( message ); 
  };
ws.send('test')
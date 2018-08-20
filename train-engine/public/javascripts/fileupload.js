function handleFileSelect(){
		alert("fileadded")
		var reader = new FileReader();
		reader.onload = function (r) {
    		var line = r.target.result.split("\n")[0].split(",")
    		line.forEach(function(l,i ){
    			$("#setupform").append("<label>column "+ i +"</label><select><option>Input</option><option>Output</option></select>")
    		})
  		};
		var f = event.target.files[0]
		var x = reader.readAsBinaryString(f.slice(0,100))
	}

	document.getElementById('customFile').addEventListener('change', handleFileSelect, false);

class EnvrionmentSetup extends React.Component {
  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
  this.state =
  {
    "filefields": (<div></div>),
  }}

  handleFileSelect(e){
    var t = this
    var reader = new FileReader();
    reader.onload = function (r) {
        var line = r.target.result.split("\n")[0].split(",")
        var columns = line.map(function(l,i ){

          return (<div>
            <label>Column {i}</label>
                  <select className="fieldmap">
                    <option>Input</option>
                    <option>Output</option>
                  </select>
              </div>)
        })

        t.setState({"filefields": columns})
      };
    var f = e.target.files[0]
    var x = reader.readAsBinaryString(f.slice(0,100))
  }

  handleSubmit(e){
    var x = $(".fieldmap").map(function(){
      return this.value
    })
    $("#fieldmap").attr('value', x.get())
    return true
  }

  


  render() {
    var d = this.props.data
    
    return (<div>
             <form id="setupform" action="/config/createenvironment" method="POST" onSubmit={(e)=>this.handleSubmit(e)} enctype="multipart/form-data">
              <label>Name the environment</label>
              <input name="environmentName" type="text"></input>
                <div className="custom-file">
                  <input type="hidden" name="csrfToken" value={$('input[name="csrfToken"]').attr('value')}></input>
                  <input type="file"  name="file" className="custom-file-input" id="customFile" onChange={(e)=>this.handleFileSelect(e)}></input>
                  <label className="custom-file-label" htmlFor="customFile">Choose file</label>
                </div>
                <div id="fieldmapping">
                {this.state.filefields}
                </div>
                <input id="fieldmap" type="hidden" name="fieldmapping"></input>
                <button type="submit">Create</button>
            </form>   
            </div>)
  }
}

ReactDOM.render(
      <EnvrionmentSetup />,
      document.getElementById('root')
  );
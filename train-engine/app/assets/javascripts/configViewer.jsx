class ConfigViewer extends React.Component {
  
  render() {
    var d = this.props.data
    var listitems = Object.keys(d).map(function(configItem){ 
        return (
          <div className="form-group row">
          <label className= "col-sm-4 col-form-label">{configItem}</label>
          <div className="col-sm-6">
          <input className="form-control form-control-sm" name={configItem} value={d[configItem]}></input>
          </div>
          </div>
          )})
    return (<div>
    {
      listitems.length>0?(        <form className="config-form" action="/config/reuseConfig" method="POST">
                <input type="hidden" name="csrfToken" value={$('input[name="csrfToken"]').attr('value')}></input>
               {listitems}
               <button className="btn btn-primary" type="submit">Reuse Config</button>
              </form>
              ):null
     }         
            </div>)
  }
}

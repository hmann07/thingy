class ConfigViewer extends React.Component {
  
  render() {
    var d = this.props.data
    var listitems = Object.keys(d).map(function(configItem){ 
        return (
          <div>
          <label>{configItem}</label>
          <input name={configItem} value={d[configItem]}></input>
          </div>
          )})
    return (<div>
              <form action="/config/reuseConfig" method="POST">
                <input type="hidden" name="csrfToken" value={$('input[name="csrfToken"]').attr('value')}></input>
               {listitems}
               <button type="submit">Reuse Config</button>
              </form>
              
            </div>)
  }
}

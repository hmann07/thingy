class ConfigViewer extends React.Component {
  
  render() {
    var d = this.props.data
    var listitems = Object.keys(d).map(function(configItem){ 
        return (
          React.createElement("div", null, "", 

          React.createElement("label", null, configItem), "", 

          React.createElement("input", {name: configItem, value: d[configItem]}), ""

          )
          )})
    return (React.createElement("div", null, "", 

              React.createElement("form", {action: "/config/reuseConfig", method: "POST"}, "", 

                React.createElement("input", {type: "hidden", name: "csrfToken", value: $('input[name="csrfToken"]').attr('value')}), "", 

               listitems, "", 

               React.createElement("button", {type: "submit"}, "Reuse Config"), ""

              ), ""

              

            ))
  }
}

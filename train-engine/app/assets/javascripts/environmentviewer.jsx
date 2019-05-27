class EnvironmentViewer extends React.Component {
 constructor(props) {
    super(props);
 }


 render(){
  var clickHandler = this.props.clickHandler
  const envList = this.props.value.map(function(ed){
    return (<Environment data={ed} clickHandler={clickHandler} />)
  }) 
  return (
    <div>
      {envList}
    </div>

    )
 }
}

class Environment extends React.Component {
  constructor(props) {
    super(props);
  }


  render(){
    const hrefStr = this.props.data._id.$oid
    const newRunHref = "/config/" + hrefStr
    return(<div>
              <span className="env-view-item">{this.props.data.name}</span>
              <a className="btn btn-primary btn-sm env-view-item" href={newRunHref} role="button">New Run</a>
              <a className="btn btn-primary btn-sm env-view-item" href="#" onClick={()=>this.props.clickHandler(hrefStr)} role="button">View results</a></div>)
  }

}
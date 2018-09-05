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
    return(<div>{this.props.data.name}
              <a className="btn btn-primary btn-sm" href={newRunHref} role="button">New Run</a>
              <a className="btn btn-primary btn-sm" href="#" onClick={()=>this.props.clickHandler(hrefStr)} role="button">View results</a></div>)
  }

}
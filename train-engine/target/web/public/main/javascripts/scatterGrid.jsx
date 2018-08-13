class ScatterGrid extends React.Component {
 constructor(props) {
    super(props);
  this.state =
  {
    
    "runData":[]
  }
 }

 componentDidMount(){
  var t = this
  $.get( "/runs", function( runData ) {
    t.setState({runData : runData})
  })
 }

 render(){
   var t = this
    const gridData = t.state.runData.length>0?Object.keys(t.state.runData[0].settings):[]
    const gridHead = gridData.map(function(d){return (<div className="gridLabelHead">{d}</div>)})
    const grids = gridData.map(function(g1){
      return (<div>
        <div className="gridLabel">{g1}</div>
          {
        gridData.map(function(g2){
          return (<ScatterGraph data={t.state.runData} axis={[g2, g1, "bestPerformance"]} />)
        })}
        <br />
        </div>)
    })
    return (
    <div>
        <div className="gridHeadContainer">
        {gridHead}
        </div>
        {grids}
    </div>

    )
 }

 }


ReactDOM.render(
      <ScatterGrid />,
      document.getElementById('root')
  );


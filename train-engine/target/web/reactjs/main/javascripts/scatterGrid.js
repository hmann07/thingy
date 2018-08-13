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
    const gridHead = gridData.map(function(d){return (React.createElement("div", {className: "gridLabelHead"}, d))})
    const grids = gridData.map(function(g1){
      return (React.createElement("div", null, "", 

        React.createElement("div", {className: "gridLabel"}, g1), "", 

          
        gridData.map(function(g2){
          return (React.createElement(ScatterGraph, {data: t.state.runData, axis: [g2, g1, "bestPerformance"]}))
        }), "", 

        React.createElement("br", null), ""

        ))
    })
    return (
    React.createElement("div", null, "", 

        React.createElement("div", {className: "gridHeadContainer"}, "", 

        gridHead, ""

        ), "", 

        grids, ""

    )

    )
 }

 }


ReactDOM.render(
      React.createElement(ScatterGrid, null),
      document.getElementById('root')
  );


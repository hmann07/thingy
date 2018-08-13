class App extends React.Component {
 constructor(props) {
    super(props);
  this.state =
  {
    "generationData": [],
    "runData":[],
    "speciesData": [],
    "networksData": [],
    "currentRunSettings":"{}",
    currentGeneration: 0,
    currentSpecies: 0,
    currentRunId: 0
  }
 }

 componentDidMount(){
  var t = this
  $.get( "/runs", function( runData ) {
    t.setState({runData : runData})
  })
 }



 runClickHandle(runData) {
  var t = this
  $.get( "/generations/" + runData.runId, function( generationData ) {
    t.setState({generationData : generationData, currentRunId : runData.runId, currentRunSettings: runData.settings})
  })  
   
 }


 generationClickHandle(generationData) {
  var t = this
  $.get( "/getSpeciesByGeneration/" + t.state.currentRunId + "/" + generationData.generation, function( speciesData ) {
    t.setState({speciesData : speciesData, currentGeneration : generationData.generation})
   }) 
   
 }

  speciesClickHandle(speciesData) {
  var t = this
  var species = speciesData.species
  $.get( "/getNetByGenerationAndSpecies/" + t.state.currentRunId + "/" + t.state.currentGeneration  + "/" + species, function( netData ) {
    t.setState({networksData : netData.sort(function(a,b){return a.fitness>b.fitness?-1:(a.fitness<b.fitness?1:0)})})
  })
 }

 netClickHandle(species) {
  alert("hello")
 }

 render(){
  return (
    React.createElement("div", null, "", 

      React.createElement(DataSelector, {value: this.state.runData, fields: ["runId","startTime", "endTime", "duration", "bestFitness", "bestPerformance"], clickHandler: this.runClickHandle.bind(this)}), "", 

      React.createElement(ConfigViewer, {data: this.state.currentRunSettings}), "", 

      React.createElement(BarChart, {data: this.state.generationData}), "", 

      React.createElement(DataSelector, {value: this.state.generationData, fields: ["generation","bestPerformance", "bestFitness"], clickHandler: this.generationClickHandle.bind(this)}), "", 

      React.createElement(DataSelector, {value: this.state.speciesData, fields: ["species", "speciesTotalFitness", "speciesBestFitness"], clickHandler: this.speciesClickHandle.bind(this)}), "", 

      React.createElement(DataSelector, {value: this.state.networksData, fields: "all", clickHandler: this.netClickHandle.bind(this)}), ""

    )

    )
 }

 }


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


class DataPager extends React.Component {
  
  renderLi(itemNo) {
    return (React.createElement("li", {key: itemNo, className: "page-link", onClick: ()=>this.props.changeHandler(itemNo)}, itemNo))
  }

  render() {
    var pagers = []
    for (var i = 0; i < this.props.pages; i++){
      var k = this.renderLi(i)
      pagers.push(k)
    }

    return pagers;
  }
}


class DataSelector extends React.Component {

  constructor(props) {
    super(props);
          
    this.state = {
            currentPage: 0
      };
    }


  pageChangeHandler(x) {
    
    this.setState({currentPage : x})
    
  }


  render() {
    var pages = this.props.value.reduce(function(ar, it, i) { 
      const ix = Math.floor(i/10); 

      if(!ar[ix]) {
        ar[ix] = [];
      }
    ar[ix].push(it);
      return ar;
    }, [])
    var totalPages =  pages.length
    var t = this.props.fields
    var tt = this.props.clickHandler
    return (
      React.createElement("div", null, "", 

        React.createElement("table", {className: "table table-hover"}, "", 

          React.createElement("thead", null, "", 

            React.createElement("tr", null, "", 

              t =="all"?null: t.map(function(x){ return(React.createElement("th", {scope: "col"}, x))}), ""

            ), ""

          ), "", 

            React.createElement("tbody", {className: "list"}, "", 

              
              pages.length > 0 ?  
                pages[this.state.currentPage].map(function(page) {    
                return (React.createElement(DataSelectorItem, {key: page._id["$oid"], value: page, fields: t, clickHandler: tt})) 
              }): null, 
              ""

            ), ""

          ), "", 

          React.createElement("ul", {className: "pagination"}, "", 

            React.createElement(DataPager, {pages: totalPages, changeHandler: this.pageChangeHandler.bind(this)}), ""

          ), ""

        )
    );
  }
}


class DataSelectorItem extends React.Component {

  componentDidUpdate() {
    this.props.fields == "all" ? drawSim(this.props.value,this.node): null
    }

    componentDidMount() {
    this.props.fields == "all" ? drawSim(this.props.value,this.node): null
    }

  render() {
    const data =  this.props
    const t = this.props.fields == "all" ? (
            React.createElement("td", {ref: node => this.node = node, className: "list-item"}, ""

                 

            )
            ) :
            this.props.fields.map(function(field){
              return (
              React.createElement("td", {className: "list-item", onClick: ()=>data.clickHandler(data.value)}, "", 

          data.value[field], ""

              )
            )})
  
    return (
      React.createElement("tr", null, "", 

        t, ""

        )
    );
  }
}

ReactDOM.render(
      React.createElement(App, null),
      document.getElementById('root')
  );


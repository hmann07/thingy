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
    <div>
      <DataSelector value={this.state.runData} fields={["runId","startTime", "endTime", "duration", "bestFitness", "bestPerformance"]} clickHandler={this.runClickHandle.bind(this)}/>
      <ConfigViewer data={this.state.currentRunSettings} />
      <BarChart data={this.state.generationData}/>
      <DataSelector value={this.state.generationData} fields={["generation","bestPerformance", "bestFitness"]} clickHandler={this.generationClickHandle.bind(this)}/>
      <DataSelector value={this.state.speciesData} fields={["species", "speciesTotalFitness", "speciesBestFitness"]} clickHandler={this.speciesClickHandle.bind(this)}/>
      <DataSelector value={this.state.networksData} fields="all" clickHandler={this.netClickHandle.bind(this)}/>
    </div>

    )
 }

 }




class DataPager extends React.Component {
  
  renderLi(itemNo) {
    return (<li key={itemNo} className="page-link" onClick={()=>this.props.changeHandler(itemNo)}>{itemNo}</li>)
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
      <div>
        <table className="table table-hover">
          <thead>
            <tr>
              {t =="all"?null: t.map(function(x){ return(<th scope="col">{x}</th>)})}
            </tr>
          </thead>
            <tbody className="list">
              {
              pages.length > 0 ?  
                pages[this.state.currentPage].map(function(page) {    
                return (<DataSelectorItem  key={page._id["$oid"]} value={page} fields={t} clickHandler={tt} />) 
              }): null
              }
            </tbody>
          </table>
          <ul className="pagination">
            <DataPager pages={totalPages} changeHandler={this.pageChangeHandler.bind(this)} />
          </ul>
        </div>
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
    const genomeStr = JSON.stringify(data.value)
    const t = this.props.fields == "all" ? (
            <tr>
            <td ref={node => this.node = node} className="list-item">
                 
            </td>
            <td>
              <form action="/submitgenome" method="POST">
               <input type="hidden" name="csrfToken" value={$('input[name="csrfToken"]').attr('value')}></input>
                <input type="hidden" name="genome" value={genomeStr}></input>
              <button type="submit">Use Genome</button>
              </form>
            </td>
            </tr>
            ) :
            <tr>
            {
            this.props.fields.map(function(field){
              return (
              
              <td className="list-item" onClick={()=>data.clickHandler(data.value)}>
          {data.value[field]}
              </td>

            )})
          }
            </tr>
  
    return t;
  }
}

ReactDOM.render(
      <App />,
      document.getElementById('root')
  );


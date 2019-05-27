class App extends React.Component {
 constructor(props) {
    super(props);
  this.state =
  {
    "environmentData":[],
    "generationData": [],
    "runData":[],
    "speciesData": [],
    "networksData": [],
    "currentRunSettings":{},
    "selectedRunId": "",
    "selectedGenerationId": "",
    "selectedSpeciesId": "",
    currentGeneration: 0,
    currentSpecies: 0,
    currentRunId: 0
  }
 }

 componentDidMount(){
  var t = this
  $.get( "/environments", function( environmentData ) {
    t.setState({environmentData : environmentData})
  })
 }


envClickHandle(envData) {
  var t = this
  $.get( "/runs/" + envData, function( runData ) {
    t.setState({runData : runData,
                generationData:[],
                speciesData: [],
                networksData: [],
                currentRunSettings:{}

                })
  })   
 }


 runClickHandle(runData) {
  var t = this
  $.get( "/generations/" + runData.runId, function( generationData ) {
    t.setState({generationData : generationData, 
                selectedRunId: runData._id.$oid,
                currentRunId : runData.runId, currentRunSettings: runData.settings, 
                speciesData: [],
                networksData: []
                
              })
  })   
 }


 generationClickHandle(generationData) {
  var t = this
  $.get( "/getSpeciesByGeneration/" + t.state.currentRunId + "/" + generationData.generation, function( speciesData ) {
    t.setState({speciesData : speciesData,
                selectedGenerationId: generationData._id.$oid,
                currentGeneration : generationData.generation, networksData: []})
   }) 
   
 }

  speciesClickHandle(speciesData) {
  var t = this
  var species = speciesData.species
  $.get( "/getNetByGenerationAndSpecies/" + t.state.currentRunId + "/" + t.state.currentGeneration  + "/" + species, function( netData ) {
    t.setState({
          selectedSpeciesId: speciesData._id.$oid,
          networksData : netData.sort(function(a,b){return a.fitness>b.fitness?-1:(a.fitness<b.fitness?1:0)})})
  })
 }

 netClickHandle(species) {
  alert("hello")
 }

 render(){
  return (
    <div>
      <EnvironmentViewer value={this.state.environmentData} clickHandler={this.envClickHandle.bind(this)}/>
      <DataSelector selectedDataItem={this.state.selectedRunId} 
                    value={this.state.runData} 
                    fields={["runId","startTime", "endTime", "duration", "bestFitness", "bestPerformance"]} 
                    clickHandler={this.runClickHandle.bind(this)}/>
      
      <ConfigViewer data={this.state.currentRunSettings} />
      <BarChart data={this.state.generationData}/>
     
      <DataSelector selectedDataItem={this.state.selectedGenerationId} value={this.state.generationData} fields={["generation","bestPerformance", "bestFitness"]} clickHandler={this.generationClickHandle.bind(this)}/>
      <DataSelector selectedDataItem={this.state.selectedSpeciesId} value={this.state.speciesData} fields={["species", "speciesTotalFitness", "speciesBestFitness"]} clickHandler={this.speciesClickHandle.bind(this)}/>
      <DataSelector value={this.state.networksData} environmentId={this.state.currentRunSettings.environmentId} fields="all" clickHandler={this.netClickHandle.bind(this)}/>
    </div>

    )
 }

 }




class DataPager extends React.Component {
  
  renderLi(itemNo) {
    const liClass = this.props.currentPage == itemNo?"page-item active":"page-item"
    return (<li key={itemNo} className={""+ liClass + ""} onClick={()=>this.props.changeHandler(itemNo)}><a class="page-link">{itemNo}</a></li>)
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
    var si = this.props.selectedDataItem
    var totalDataLength = this.props.value.length
    var totalPages =  pages.length
    var t = this.props.fields
    var tt = this.props.clickHandler
    var env = this.props.environmentId

    return (
      <div>
          {totalDataLength > 0 ?
            (
              <div className="data-selector">
          <ul className="pagination">
            <DataPager currentPage={this.state.currentPage} pages={totalPages} changeHandler={this.pageChangeHandler.bind(this)} />
          </ul>
        <table className="table table-hover table-striped">
          <thead>
            <tr>
              {t =="all"?null: t.map(function(x){ return(<th scope="col">{x}</th>)})}
            </tr>
          </thead>
            <tbody className="list">
              {
              pages.length > 0 ?  
                pages[this.state.currentPage].map(function(page) {    
                return (<DataSelectorItem  selectedDataItem={si} key={page._id["$oid"]} value={page} fields={t} envId={env} clickHandler={tt} />) 
              }): null
              }
            </tbody>
          </table>
          </div>
          ) : null
         } 
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
    const rowClass = data.value._id?(this.props.selectedDataItem == data.value._id.$oid?"data-selector-item table-info":"data-selector-item"):null
    const t = this.props.fields == "all" ? (
            <tr className={"" + rowClass + ""}>
            <td ref={node => this.node = node} className="list-item">
                 
            </td>
            <td>
              <form action="/submitgenome" method="POST">
               <input type="hidden" name="csrfToken" value={$('input[name="csrfToken"]').attr('value')}></input>
                <input type="hidden" name="genome" value={genomeStr}></input>
                <input type="hidden" name="envId" value={data.envId}></input>
              <button className="btn btn-primary" type="submit">Use Genome</button>
              </form>
            </td>
            </tr>
            ) :
            <tr className={"" + rowClass + ""}>
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


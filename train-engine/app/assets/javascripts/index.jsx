class App extends React.Component {
 constructor(props) {
		super(props);
 	this.state =
 	{
 		"generationData": this.props.value,
 		"speciesData": [],
 		"networksData": [],
 		currentGeneration: 0,
 		currentSpecies: 0
 	}
 }

 generationClickHandle(generation) {
 	var t = this
 	$.get( "/getSpeciesByGeneration/" + generation, function( speciesData ) {
		t.setState({speciesData : speciesData, currentGeneration : generation})
	})	
 	 
 }

  speciesClickHandle(species) {
 	var t = this
 	$.get( "/getNetByGenerationAndSpecies/" + t.state.currentGeneration  + "/" + species, function( netData ) {
		t.setState({networksData : netData})
	})
 }

 netClickHandle(species) {
 	alert("hello")
 }

 render(){
 	return (
 		<div>
 			<DataSelector value={this.state.generationData} fields={["generation","bestPerformance", "bestFitness"]} clickHandler={this.generationClickHandle.bind(this)}/>
 			<DataSelector value={this.state.speciesData} fields={["species", "speciesTotalFitness"]} clickHandler={this.speciesClickHandle.bind(this)}/>
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
	    	<table>
	    		<thead>
	    			<tr>
	    				{t =="all"?null: t.map(function(x){ return(<th>{x}</th>)})}
	    			</tr>
	    		</thead>
	      		<tbody className="list">
	      			{
	      			pages.length > 0 ?	
	      	    	pages[this.state.currentPage].map(function(page) {  	
	      				return (<DataSelectorItem  key={page._id.$oid} value={page} fields={t} clickHandler={tt} />)	
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
  	const t = this.props.fields == "all" ? (
      			<td ref={node => this.node = node} className="list-item">
      			   	 
      			</td>
      			) :
      			this.props.fields.map(function(field){
      				return (
      				<td className="list-item" onClick={()=>data.clickHandler(data.value[field])}>
     			{data.value[field]}
      				</td>
      			)})
  
    return (
    	<tr>
      	{t}
      	</tr>
    );
  }
}


$.get( "/generations", function( generations ) {
	ReactDOM.render(
  		<App value={generations} fields="generation"/>,
  		document.getElementById('root')
	);
})


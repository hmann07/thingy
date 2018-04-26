class App extends React.Component {
 constructor(props) {
		super(props);
 	this.state =
 	{
 		"generationData": this.props.value,
 		"speciesData": []
 	}
 }

 generationClickHandle(generation) {
 	var t = this
 	$.get( "http://localhost:9000/getSpeciesByGeneration/" + generation, function( speciesData ) {
		t.setState({speciesData : speciesData})
	})	
 	 
 }

  speciesClickHandle(species) {
 	alert("hello")
 }

 render(){
 	return (
 		<div>
 			<DataSelector value={this.state.generationData} fields="generation" clickHandler={this.generationClickHandle.bind(this)}/>
 			<DataSelector value={this.state.speciesData} fields="species" clickHandler={this.speciesClickHandle.bind(this)}/>
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
      		<ul className="list">
      			{
      			pages.length > 0 ?	
      	    	pages[this.state.currentPage].map(function(page) {  	
      				return (<DataSelectorItem  key={page._id.$oid} value={page} fields={t} clickHandler={tt} />)	
      			}): null
      			}
      		</ul>
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
  	const t = this.props.fields == "all" ? (
      			<li ref={node => this.node = node} className="list-item">
      			   	 
      			</li>
      			) :
      			(
      				<li className="list-item" onClick={()=>this.props.clickHandler(this.props.value.generation)}>
     			{this.props.fields + this.props.value[this.props.fields]}
      				</li>
      			)
  
    return (
      t
    );
  }
}


$.get( "http://localhost:9000/generations", function( generations ) {
	ReactDOM.render(
  		<App value={generations} fields="generation"/>,
  		document.getElementById('root')
	);
})


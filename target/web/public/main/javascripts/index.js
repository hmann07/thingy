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
 		React.createElement("div", null, 
 			React.createElement(DataSelector, {value: this.state.generationData, fields: "generation", clickHandler: this.generationClickHandle.bind(this)}), 
 			React.createElement(DataSelector, {value: this.state.speciesData, fields: "species", clickHandler: this.speciesClickHandle.bind(this)})
 		)

 		)
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
    	React.createElement("div", null, 
      		React.createElement("ul", {className: "list"}, 
      			
      			pages.length > 0 ?	
      	    	pages[this.state.currentPage].map(function(page) {  	
      				return (React.createElement(DataSelectorItem, {key: page._id.$oid, value: page, fields: t, clickHandler: tt}))	
      			}): null
      			
      		), 
      		React.createElement("ul", {className: "pagination"}, 
      			React.createElement(DataPager, {pages: totalPages, changeHandler: this.pageChangeHandler.bind(this)})
      		)
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
  	const t = this.props.fields == "all" ? (
      			React.createElement("li", {ref: node => this.node = node, className: "list-item"}
      			   	 
      			)
      			) :
      			(
      				React.createElement("li", {className: "list-item", onClick: ()=>this.props.clickHandler(this.props.value.generation)}, 
     			this.props.fields + this.props.value[this.props.fields]
      				)
      			)
  
    return (
      t
    );
  }
}


$.get( "http://localhost:9000/generations", function( generations ) {
	ReactDOM.render(
  		React.createElement(App, {value: generations, fields: "generation"}),
  		document.getElementById('root')
	);
})


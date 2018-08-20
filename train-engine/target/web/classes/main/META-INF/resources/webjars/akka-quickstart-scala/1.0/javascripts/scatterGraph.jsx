class ScatterGraph extends React.Component {
   constructor(props){
      super(props)
      this.createScatterGraph = this.createScatterGraph.bind(this)
    }

   componentDidUpdate() {
      this.createScatterGraph()
   }
   
   componentDidMount(){
      this.createScatterGraph()
   }

   createScatterGraph() {
      const node = d3.select(this.node).select("g")
      const  xAttr = this.props.axis[0]
      const  yAttr = this.props.axis[1]
      const  cAttr = this.props.axis[2]
      const fval = this.props.filterVal
      const filteredData = this.props.data.filter(function(d){return d[cAttr] > fval })

      const yScale = d3.scaleLinear()
         .domain(d3.extent(this.props.data, function(d){return d.settings[yAttr]}))
         .range([80,0])

      const xScale = d3.scaleLinear()
         .domain(d3.extent(this.props.data, function(d){return d.settings[xAttr]}))
         .range([0, 80])


      const cScale = d3.scaleLinear().range([0,1]).domain(d3.extent(this.props.data, function(d){return d[cAttr]}))

      const axis = d3.axisLeft(yScale);

      function buildForm(d){
         // alert(d)
          ReactDOM.render(
              <ConfigViewer data={d.settings} />,
         document.getElementById('config')
          );
      }
   
   node
      .selectAll('circle')
      .data(filteredData)
      .enter()
      .append('circle')
      .on("click", function(d){
        buildForm(d)
      })
      .style("fill", d => d3.interpolateRdYlBu(cScale(d[cAttr])))
      .style("opacity", 0.6)
      .attr('r', 5)
      .attr('cx', (d,i) => xScale(d.settings[xAttr]))
      .attr('cy', d => yScale(d.settings[yAttr]))
      .append("title")
      .text(function(d) { return xAttr + ": " +  d.settings[xAttr] + ", " + yAttr + ": " + d.settings[yAttr] + ", val: " +  d[cAttr] ; })
      ;
   
   node
      .selectAll('circle')
      .data(filteredData)
      .exit()
      .remove()
   
   node
      .selectAll('circle')
      .data(filteredData)
      .style("fill", d => d3.interpolateRdYlBu(cScale(d[cAttr])))
      .style("opacity", 0.6)
      .attr('r', 5)
      .attr('cx', (d,i) => xScale(d.settings[xAttr]))
      .attr('cy', d => yScale(d.settings[yAttr]))
      .on("click", function(d){
        buildForm(d)
      })
      .append("title")
      .text(function(d) { return xAttr + ": " +  d[xAttr] + ", " + yAttr + ": " + d[yAttr] + ", val: " +  d[cAttr] ; })
      
      ;
   
 
 /*     
   node
      .selectAll(".xaxis").data([1])
      .call(axis);

   node
      .selectAll(".xaxis").data([1])
      .enter()
      .append("g")
      .attr("class", "xaxis")
      .attr("transform", "translate(0,0)")
      .call(axis);

      */
   }
render() {
      return <svg className="smSVG" ref={node => this.node = node}
      width={80} height={80}>
      <g transform="translate(5,0)" className="plotarea"></g>
      </svg>
   }
 }
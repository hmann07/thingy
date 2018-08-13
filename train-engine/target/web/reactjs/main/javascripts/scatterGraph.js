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

      const yScale = d3.scaleLinear()
         .domain(d3.extent(this.props.data, function(d){return d.settings[yAttr]}))
         .range([80,0])

      const xScale = d3.scaleLinear()
         .domain(d3.extent(this.props.data, function(d){return d.settings[xAttr]}))
         .range([0, 80])


      const cScale = d3.scaleLinear().range([0,1]).domain(d3.extent(this.props.data, function(d){return d[cAttr]}))

      const axis = d3.axisLeft(yScale);
   
   node
      .selectAll('circle')
      .data(this.props.data)
      .enter()
      .append('circle')
      .style("fill", d => d3.interpolateRdYlBu(cScale(d[cAttr])))
      .style("opacity", 0.6)
      .attr('r', 5)
      .attr('cx', (d,i) => xScale(d.settings[xAttr]))
      .attr('cy', d => yScale(d.settings[yAttr]))
      .append("title")
      .text(function(d) { return xAttr + ": " +  d.settings[xAttr] + ", " + yAttr + ": " + d.settings[yAttr] + ", val: " +  d[cAttr] ; });
   
   node
      .selectAll('circle')
      .data(this.props.data)
      .exit()
      .remove()
   
   node
      .selectAll('circle')
      .data(this.props.data)
      .style("fill", d => d3.interpolateRdYlBu(cScale(d[cAttr])))
      .style("opacity", 0.6)
      .attr('r', 5)
      .attr('cx', (d,i) => xScale(d.settings[xAttr]))
      .attr('cy', d => yScale(d.settings[yAttr]))
      .append("title")
      .text(function(d) { return xAttr + ": " +  d[xAttr] + ", " + yAttr + ": " + d[yAttr] + ", val: " +  d[cAttr] ; });
   
 
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
      return React.createElement("svg", {className: "smSVG", ref: node => this.node = node, 
      width: 80, height: 80}, "", 

      React.createElement("g", {transform: "translate(5,0)", className: "plotarea"}), ""

      )
   }
 }
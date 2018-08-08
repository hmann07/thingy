class BarChart extends React.Component {
   constructor(props){
      super(props)
      this.createBarChart = this.createBarChart.bind(this)
   }
   componentDidMount() {
      this.createBarChart()
   }
   componentDidUpdate() {
      this.createBarChart()
   }
   createBarChart() {
      const node = d3.select(this.node).select("g")
      const dataMax = d3.max(this.props.data, function(d){return d.bestPerformance})
      const yScale = d3.scaleLinear()
         .domain([dataMax, 0])
         .range([0,400])

      const xScale = d3.scaleBand()
         .domain(this.props.data.map(function(d){return d.generation}))
         .range([0, 400])

      const axis = d3.axisLeft(yScale);
   
   node
      .selectAll('rect')
      .data(this.props.data)
      .enter()
      .append('rect')
   
   node
      .selectAll('rect')
      .data(this.props.data)
      .exit()
      .remove()
   
   node
      .selectAll('rect')
      .data(this.props.data)
      .style('fill', '#fe9922')
      .attr('x', (d,i) => xScale(i))
      .attr('y', d => yScale(d.bestPerformance))
      .attr('height', d => 400- yScale(d.bestPerformance))
      .attr('width', xScale.bandwidth())

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
   }
render() {
      return React.createElement("svg", {ref: node => this.node = node, 
      width: 500, height: 500}, "", 

      React.createElement("g", {transform: "translate(30,0)", className: "plotarea"}), ""

      )
   }
}
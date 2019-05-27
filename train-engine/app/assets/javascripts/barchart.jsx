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
      const dataMax = d3.max(this.props.data, function(d){return d.bestFitness})
      const yScale = d3.scaleLinear()
         .domain([dataMax, 0])
         .range([0,400])

      const xScale = d3.scaleBand()
         .domain(this.props.data.map(function(d){return d.generation}))
         .range([0, 400])

      var ticks = xScale.domain().filter(function(d,i){ return !(i%25); } );
     

      const axis = d3.axisLeft(yScale);
      const xaxis = d3.axisBottom(xScale).tickValues(ticks);
   

   

   node.selectAll(".info-text")
         .data(["this is going to be infor text"])
         .enter()
         .append("text")
         .text(function(d){return d})
         .attr("transform", function(d){
                  return "translate("+  (400 - this.textLength.baseVal.value) + ",0)"
               })
         .attr("class", "info-text")
         
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
      .attr('y', d => yScale(d.bestFitness))
      .attr('height', d => 400- yScale(d.bestFitness))
      .attr('width', xScale.bandwidth())
      .on("mouseover", function(d){
            d3.select(".info-text")
                  .text("generation: " + d.generation + ", Fitness: " + d.bestFitness)
                  .attr("transform", function(f){
                  return "translate("+  (400 - this.textLength.baseVal.value) + ",0)"
               })
      })

   node
      .selectAll(".yaxis").data([1])
      .call(axis);

   node
      .selectAll(".yaxis").data([1])
      .enter()
      .append("g")
      .attr("class", "yaxis axis")
      .attr("transform", "translate(0,0)")
      .call(axis);

   node
      .selectAll(".xaxis").data([1])
      .call(xaxis);

   node
      .selectAll(".xaxis").data([1])
      .enter()
      .append("g")
      .attr("class", "xaxis axis")
      .attr("transform", "translate(0,400)")
      .call(xaxis);

   }
render() {
      return (
       <div>
       {
        this.props.data.length > 0? (
         <svg ref={node => this.node = node}
        
      width={500} height={500}>
      <g transform="translate(75,50)" className="plotarea"></g>
      </svg>):null
      }
      </div>
      )
   }
}
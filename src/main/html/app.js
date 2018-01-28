
// Core taken from : https://bl.ocks.org/mbostock/4600693
// arrow heads and inspiration for tick function from : https://codepen.io/zarazum/pen/fjoqF.

// set up SVG for D3
var width  = 800,
    height = 500,
    colors = function(){ return "#FFF";};//d3.scale.category10();




var simulation = d3.forceSimulation()
    .force("link", d3.forceLink().id(function(d) { return d.id; }))
    .force("charge", d3.forceManyBody())
    .force("center", d3.forceCenter(width / 2, height / 2))
    .force("collide", d3.forceCollide(50));
// set up initial nodes and links
//  - nodes are known by 'id', not by index in array.
//  - reflexive edges are indicated on the node (as a bold black circle).
//  - links are always source < target; edge directions are set by 'left' and 'right'.

drawSim(genome)

function drawSim(genome) {
  var svg = d3.select('#networkViewer')
  .append('svg')
  .attr('width', width)
  .attr('height', height);


svg.append('svg:defs').append('svg:marker')
    .attr('id', 'end-arrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 16)
    .attr('markerWidth', 13)
    .attr('markerHeight', 12)
    .attr('orient', 'auto')
  .append('svg:path')
    .attr('d', 'M0,-5L10,0L0,5')
    .attr('fill', '#666666');

svg.append('svg:defs').append('svg:marker')
    .attr('id', 'start-arrow')
    .attr('viewBox', '0 -5 10 10')
    .attr('refX', 4)
    .attr('markerWidth', 3)
    .attr('markerHeight', 2 )
    .attr('orient', 'auto')
  .append('svg:path')
    .attr('d', 'M10,-5L0,0L10,5')
    .attr('fill', '#66666');
  var nodes = genome.neurons
  var outputlinks = genome.connections.filter(function(l){return l.enabled})
  var links = outputlinks.map(function(l) {
      return {
      source: nodes.filter(function( n ) {  return n.id == l.from;})[0],
      target: nodes.filter(function( n ) {  return n.id == l.to;})[0] ,
      left: l.left,
      right: l.right,
      weight: l.weight
      }
    })

  var linkWidthScale = d3.scaleLinear().range([3,6]).domain([d3.min(outputlinks, function(d){return d.weight}), d3.max(outputlinks,function(d){return d.weight})]);
  var xPosScale =  d3.scaleLinear().range([50, width - 50]).domain([0,1])
  var actFnColourScale = d3.scaleOrdinal().domain(["SIGMOID", "GAUSSIAN", "SINE", "TANH", "BIPOLARSIGMOID", "IDENTITY"]).range(d3.schemeCategory20);
  var nodeSize = 20
  var color = d3.scaleOrdinal(d3.schemeCategory20);
  var link = svg.selectAll(".link")
    .data(links)
    .enter().append("path")
      .attr("class", "link")
     // .style("stroke-width", function(d){ return linkWidthScale(d.weight) + "px"})
      .style('marker-mid', 'url(#end-arrow)')
      //.style('marker-start', function(d) { return d.left ? 'url(#start-arrow)' : ''; })
    //.style('marker-end', function(d) { return d.right ? 'url(#end-arrow)' : ''; })
      ;

  var nodeSelection = svg.append("g")
    .attr("class", "nodes")
    .selectAll("circle")
    .data(nodes)
    
  var node = nodeSelection.enter().append("circle")
      .attr("r", nodeSize)
      .attr("fill", function(d) {
        return d.subnetid?"#999999":actFnColourScale(d.activationFunction);
      })
      .on("click",function(d){
        if(d.subnetid) {
          drawSim(genome.subnets.filter(function( obj ) {
            return obj.id == d.subnetid;
          })[0])}
      })
      .call(d3.drag()
          .on("start", dragstarted)
          .on("drag", dragged)
          .on("end", dragended));

  node.append("title")
      .text(function(d) {
          return d.activationFunction;
           });

  simulation
      .nodes(nodes)
      .on("tick", ticked);

  simulation.force("link")
      .links(links);

  function ticked() {
    link.attr("d",  function(d) {
    if(d.target.id == d.source.id){
      return 'M' + (xPosScale(d.target.layer)) + ',' + (d.target.y) +  'A30, 30 -160, 1, 1,' + (xPosScale(d.target.layer)  ) +  ',' + (d.target.y )
    }else {
      var deltaX = xPosScale(d.target.layer) - xPosScale(d.source.layer),
          deltaY = d.target.y - d.source.y,
          dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY),
          normX = deltaX / dist,
          normY = deltaY / dist,
          sourcePadding = 0 //d.left ? nodeSize + 6 : nodeSize + 3,
          targetPadding = 0 //d.right ? nodeSize + 6 : nodeSize + 3,
          sourceX = xPosScale(d.source.layer) + (sourcePadding * normX),
          sourceY = d.source.y + (sourcePadding * normY),
          targetX = xPosScale(d.target.layer) - (targetPadding * normX),
          targetY = d.target.y - (targetPadding * normY);
      return 'M' + sourceX + ',' + sourceY + 'A' + dist*(1) + ',' + dist*(1) + ' 0 0,'+ (sourceY > targetY && sourceX < targetX?0:1)  +' ' + '' + targetX + ',' + targetY;
  }
  })
  
  node.attr("cx", function(d) { return xPosScale(d.layer); })
      .attr("cy", function(d) { return d.y; });
  }



}







  


function dragstarted(d) {
  if (!d3.event.active) simulation.alphaTarget(0.3).restart();
  d.fx = d.x;
  d.fy = d.y;

}

function dragged(d) {
  d.fx = d3.event.x;
  d.fy = d3.event.y;
}

function dragended(d) {
  if (!d3.event.active) simulation.alphaTarget(0);
  //d.fx = null;
  //d.fy = null;

}
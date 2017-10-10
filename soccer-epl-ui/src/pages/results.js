import React from 'react';
import {
  Table,
  TableBody,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
} from 'material-ui/Table';

class Results extends React.Component {
	constructor() {
	  	super();
	 	this.state={resultsByDate: [],
	 	fixedHeader: true,	    
	    stripedRows: true,
	    showRowHover: true,	
	    showCheckboxes: false,    
	    height: '100%'};
	}

	componentDidMount(){
	  	fetch(`http://localhost:9000/results`)
	 	.then(result=>result.json())	 	
	    .then(items=>this.setState({resultsByDate: items.rows[0].doc.results}))	    
	  }

	  render() {		  
		  return (
		    <div>
		    <h2>Results</h2>

		{this.state.resultsByDate.length ? this.state.resultsByDate.map(resultByDate => 

		<Table
          height={this.state.height}         
          fixedHeader={this.state.fixedHeader}                    
        >
          <TableHeader
            displaySelectAll={this.state.showCheckboxes}            
          >            
            <TableRow>
            	<TableHeaderColumn colSpan="3" tooltip="Super Header" style={{textAlign: 'left'}}>
                	{resultByDate.date}
              	</TableHeaderColumn>
            </TableRow>
          </TableHeader>
          <TableBody
            displayRowCheckbox={this.state.showCheckboxes}            
            showRowHover={this.state.showRowHover}
            stripedRows={this.state.stripedRows}
          >  	

          	{resultByDate.results.map(result =>

          			<TableRow>			                
			                <TableRowColumn>{result.homeTeam}</TableRowColumn>
			                <TableRowColumn>{result.homeScore}</TableRowColumn>
			                <TableRowColumn>{result.awayScore}</TableRowColumn>
			                <TableRowColumn>{result.awayTeam}</TableRowColumn>
					</TableRow>	
			)}
			</TableBody>
		</Table>

		)
		: 'Loading...'}



		    </div>




		  );
	}
}

export default Results;
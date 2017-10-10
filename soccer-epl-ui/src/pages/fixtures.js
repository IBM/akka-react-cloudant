import React from 'react';
import {
  Table,
  TableBody,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
} from 'material-ui/Table';

class Fixtures extends React.Component {
	constructor() {
	  	super();
	 	this.state={fixtures:'',
	 	fixedHeader: true,	    
	    stripedRows: true,
	    showRowHover: true,	
	    showCheckboxes: false,    
	    height: '100%'};
	}

	componentDidMount(){
	  	fetch(`http://localhost:9000/fixtures`)
	 	.then(result=>result.json())	 	
	    .then(items=>this.setState({fixtures: items.rows[0].doc.games}))
	  }

	  render() {		  
		  return (
		    <div>
		    <h2>Fixtures</h2>
		    { this.state.fixtures ? Object.keys(this.state.fixtures).map((date) => 

		    	<Table
          height={this.state.height}         
          fixedHeader={this.state.fixedHeader}                    
        >
          <TableHeader
            displaySelectAll={this.state.showCheckboxes}            
          >            
            <TableRow>
            	<TableHeaderColumn colSpan="4" tooltip="Super Header" style={{textAlign: 'left'}}>
                	{date}
              	</TableHeaderColumn>
            </TableRow>
          </TableHeader>
          <TableBody
            displayRowCheckbox={this.state.showCheckboxes}            
            showRowHover={this.state.showRowHover}
            stripedRows={this.state.stripedRows}
          >          

          { 
          	this.state.fixtures[date].map(game =>
          			<TableRow>			                
			                <TableRowColumn>{game.teams[0].team.name}</TableRowColumn>
			                <TableRowColumn>{game.kickOffTime}</TableRowColumn>
			                <TableRowColumn>{game.teams[1].team.name}</TableRowColumn>
			                <TableRowColumn>{game.ground.name}</TableRowColumn>			                
					</TableRow>	
			)}
			</TableBody>
		</Table>	
		    )		    
		    : 'loading...'
		    }		    
		    </div>
		    );
	}
}

export default Fixtures;
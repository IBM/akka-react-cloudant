import React from 'react';
import {
  Table,
  TableBody,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
} from 'material-ui/Table';

class TeamStanding extends React.Component {
	constructor() {
	  	super();
	 	this.state={teamStandings:[],
	 	fixedHeader: true,	    
	    stripedRows: true,
	    showRowHover: true,	
	    showCheckboxes: false,    
	    height: '100%'};
	}

	componentDidMount(){
	  	fetch(`http://localhost:9000/teamtable`)
	 	.then(result=>result.json())	 	
	    .then(items=>this.setState({teamStandings: items.rows[0].doc.teams}))
	  }

	  render() {
		  	return(
		  		<div>
		  		<h2>EPL Team Standings</h2>
		<Table
          height={this.state.height}         
          fixedHeader={this.state.fixedHeader}                    
        >
          <TableHeader
            displaySelectAll={this.state.showCheckboxes}            
          >            
            <TableRow>
              <TableHeaderColumn tooltip="Position">Position</TableHeaderColumn>
              <TableHeaderColumn style={{width: '170px'}} tooltip="Team Name">Name</TableHeaderColumn>
              <TableHeaderColumn tooltip="Team Short Name">Short Name</TableHeaderColumn>
              <TableHeaderColumn tooltip="Played">Played</TableHeaderColumn>
              <TableHeaderColumn tooltip="Won">Won</TableHeaderColumn>
              <TableHeaderColumn tooltip="Draw">Draw</TableHeaderColumn>
              <TableHeaderColumn tooltip="Lost">Lost</TableHeaderColumn>
              <TableHeaderColumn tooltip="Goal for">Goal For</TableHeaderColumn>
              <TableHeaderColumn tooltip="Goal difference">Goal Difference</TableHeaderColumn>
              <TableHeaderColumn tooltip="Goal Against">Goal Against</TableHeaderColumn>
              <TableHeaderColumn tooltip="Points">Points</TableHeaderColumn>
            </TableRow>
          </TableHeader>
          
          <TableBody
            displayRowCheckbox={this.state.showCheckboxes}            
            showRowHover={this.state.showRowHover}
            stripedRows={this.state.stripedRows}
          >  			    			    		          
		          
          {this.state.teamStandings.length ? this.state.teamStandings.map(item =>
          				<TableRow>			                
			                <TableRowColumn>{item.position}</TableRowColumn>
			                <TableRowColumn style={{width: '200px'}}>{item.teamName}</TableRowColumn>
			                <TableRowColumn>{item.teamShortName}</TableRowColumn>
			                <TableRowColumn>{item.played}</TableRowColumn>
			                <TableRowColumn>{item.won}</TableRowColumn>
			                <TableRowColumn>{item.drawn}</TableRowColumn>
			                <TableRowColumn>{item.lost}</TableRowColumn>
			                <TableRowColumn>{item.goalFor}</TableRowColumn>
			                <TableRowColumn>{item.goalAgainst}</TableRowColumn>
			                <TableRowColumn>{item.goalDifference}</TableRowColumn>
			                <TableRowColumn>{item.points}</TableRowColumn>			                
              			</TableRow>
              			)              	  		  
             
             : <div>Loading...</div> }
             </TableBody>
             </Table>
		      </div>
          );
		   
	}
}
export default TeamStanding;

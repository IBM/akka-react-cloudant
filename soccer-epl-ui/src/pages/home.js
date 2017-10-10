import React from 'react';
import Panel from '../components/panel.js';
import RaisedButton from 'material-ui/RaisedButton';
import {Link} from 'react-router-dom'
import {
  Table,
  TableBody,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
} from 'material-ui/Table';

const style = {
  margin: 12,
};

class Dashboard extends React.Component {
  constructor() {
      super();
    this.state={
      results: [],
      standings: [],
      fixtures: [],
      fixedHeader: true,      
      stripedRows: true,
      showRowHover: true, 
      showCheckboxes: false,    
      height: '100%'};
  }

  componentDidMount(){
      fetch(`http://localhost:9000/results`)
      .then(result=>result.json())    
      .then(items=>this.setState({results: items.rows[0].doc.results}))

      fetch(`http://localhost:9000/fixtures`)
      .then(result=>result.json())    
      .then(items=>this.setState({fixtures: items.rows[0].doc.games}))     

      fetch(`http://localhost:9000/teamtable`)
      .then(result=>result.json())    
      .then(items=>this.setState({standings: items.rows[0].doc.teams}))     

    }


render() {  
  var standingsRow=[];
  var resultsRow=[];
  var fixturesRow=[];
  var fixturesKey=''
  if(this.state.standings.length){
    for(var i=0;i<4;i++){
      standingsRow.push(this.state.standings[i])      
    }
  }
  if(this.state.results.length){
    for(i=0;i<2;i++){      
      resultsRow.push(this.state.results[i])      
    }
  }
  if(this.state.fixtures){
  for (var attr in this.state.fixtures){    
      fixturesKey = attr;
      fixturesRow = this.state.fixtures[attr];
      break;    
  }
  }

  return (
    <div>
    <h2>Dashboard</h2>
      <div className="row">
        <div className="col-md-6">
          <Panel
            title="Team Standing"
            righticon={true}
          >
            <Table
          height={this.state.height}         
          fixedHeader={this.state.fixedHeader}                    
          >
          <TableHeader
            displaySelectAll={this.state.showCheckboxes}            
          >            
            <TableRow>
              <TableHeaderColumn>Position</TableHeaderColumn>              
              <TableHeaderColumn>Short Name</TableHeaderColumn>
              <TableHeaderColumn>Played</TableHeaderColumn>              
              <TableHeaderColumn>Goal Difference</TableHeaderColumn>              
              <TableHeaderColumn>Points</TableHeaderColumn>
            </TableRow>
          </TableHeader>
          <TableBody
            displayRowCheckbox={this.state.showCheckboxes}            
            showRowHover={this.state.showRowHover}
            stripedRows={this.state.stripedRows}
          >                                               
          {standingsRow.length ? standingsRow.map(item =>

                  <TableRow>                      
                      <TableRowColumn>{item.position}</TableRowColumn>                      
                      <TableRowColumn>{item.teamShortName}</TableRowColumn>
                      <TableRowColumn>{item.played}</TableRowColumn>                      
                      <TableRowColumn>{item.goalDifference}</TableRowColumn>
                      <TableRowColumn>{item.points}</TableRowColumn>                      
                  </TableRow>
            )                       
             
             : <div>Loading...</div> }
             </TableBody>
             </Table>

             <RaisedButton label="View More ->" primary={true} style={style} containerElement={<Link to="/teamstanding"/>} />

          </Panel>
        </div>

        <div className="col-md-6">
          <Panel
            title="Results"
            righticon={true}
          >
            {resultsRow.length ? resultsRow.map(resultByDate => 

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
    <RaisedButton label="View More ->" primary={true} style={style} containerElement={<Link to="/results"/>} />
          </Panel>
        </div>
      </div> 
      <div className="row">
        <div className="col-md-6">
          <Panel
            title="Fixtures"
            righticon={true}
          >
            { fixturesRow ? 

          <Table
          height={this.state.height}         
          fixedHeader={this.state.fixedHeader}                    
        >
          <TableHeader
            displaySelectAll={this.state.showCheckboxes}            
          >            
            <TableRow>
              <TableHeaderColumn colSpan="4" tooltip="Super Header" style={{textAlign: 'left'}}>
                  {fixturesKey}
                </TableHeaderColumn>
            </TableRow>
          </TableHeader>
          <TableBody
            displayRowCheckbox={this.state.showCheckboxes}            
            showRowHover={this.state.showRowHover}
            stripedRows={this.state.stripedRows}
          >          

          { 
            fixturesRow.map(game =>
                <TableRow>                      
                      <TableRowColumn>{game.teams[0].team.name}</TableRowColumn>
                      <TableRowColumn>{game.kickOffTime}</TableRowColumn>
                      <TableRowColumn>{game.teams[1].team.name}</TableRowColumn>
                      <TableRowColumn>{game.ground.name}</TableRowColumn>                     
          </TableRow> 
      )}
      </TableBody>
    </Table>               
        : 'loading...'
        }
        <RaisedButton label="View More ->" primary={true} style={style} containerElement={<Link to="/fixtures"/>} />
          </Panel>
        </div>
      </div>
    </div>
  );
  }
}

export default Dashboard;

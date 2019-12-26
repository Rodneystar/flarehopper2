import * as React from "react";


interface State {
	startTime: string;
	duration: Number;
}

interface Props {
	addTimer: ( startTime: string, duration: Number) => void;
}

export default class AddTimer extends React.Component<any, State> {
	constructor(props) {
		super(props);
		this.state = {
			startTime: "00:00",
			duration: 0
		}
		this.handleStartTime = this.handleStartTime.bind(this);
		this.handleDuration = this.handleDuration.bind(this);
		this.handleAdd = this.handleAdd.bind(this);

	}

	handleStartTime( event ) {
		this.setState( { startTime: event.target.value } )
	}

	handleDuration( event ) {
		this.setState( { duration: event.target.value } )
	}

	handleAdd() {
		this.props.addTimer( this.state.startTime, this.state.duration )
	}

	render() {
        return (
		<div id="addTimerContainer">
			<div id="addTimer" >
				<div id="startTime" >
					<div> start time </div>
					<input type="time" id="startTimeInput"  value={ this.state.startTime} onChange={ this.handleStartTime } required />
				</div>
				<div id="duration">
					<div> duration (mins) </div>
					<input type="number"  id="durationInput" value={ this.state.duration.valueOf()} required min={0} max={1440} step={30} onChange={ this.handleDuration } />
				</div>
			</div>
			<input type="button" name="addNewTimer" id="addNewTimer" value="add" onClick={ this.handleAdd }/>
		</div>
		)
    }
}
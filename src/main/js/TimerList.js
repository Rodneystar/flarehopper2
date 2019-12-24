import React from 'react';
import ReactDOM from 'react-dom';
// import ModeButton from './ModeButton.js';

export default class TimerList extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			selected: null
		}
		this.handleClick = this.handleClick.bind(this);
	}

	handleClick( index ) {
		if( this.state.selected != index) {
			this.setState({selected: index})
		} else {
			this.setState({selected: null})
		}
	}


	render() {
		const {timers} = this.props
		let renderTimerList = () => {
			let displayStartTime = (st ) => st.slice(0, -3)
			let displayDuration = (d) => Math.floor(d/60)
			let getClassName = (index) => index==this.state.selected ? "timerListItem-selected": "timerListItem"
			return timers.map( (timer, i) => 
					<div onClick={ () => this.handleClick(i) } className={getClassName(i)} key={i}> <span> {displayStartTime(timer.startTime)} </span> <span> { displayDuration( timer.duration) } </span> </div>)
		}

		return (
			<div id="listContainer">
				<button onClick={ () => this.props.deleteTimer( this.state.selected ) } > Delete </button>
				<div id="listHeader"> <h6> start time </h6> <h6> duration </h6></div>
					{renderTimerList()}
			</div>

		)
	}
}

import * as React from "react";
import { render as _render } from 'react-dom';
import { timerEvent } from "./flareDomain";

interface Props {
	timers : timerEvent[];
	deleteTimer: (index: Number) => void;
}
export default class TimerList extends React.Component<Props, any> {

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


	render()  {
		const { timers }: { timers: timerEvent[] } = this.props
		let renderTimerList = () => {
			const displayStartTime = (st ) => st.slice(0, -3)
			const displayDuration = (d) => Math.floor(d/60)
			const getClassName = (index: Number) => index==this.state.selected ? "timerListItem-selected": "timerListItem"
			return timers.map( (timer: timerEvent, i: number ) => 
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

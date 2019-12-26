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
		this.handleDelete = this.handleDelete.bind(this);

	}

	handleClick( index ) {
		if( this.state.selected != index) {
			this.setState({selected: index})
		} else {
			this.setState({selected: null})
		}
	}

	handleDelete = function() {
		this.props.deleteTimer( this.state.selected )
		this.setState( { selected: null } )
	}

	render()  {
		const { timers }: { timers: timerEvent[] } = this.props
		let renderTimerList = () => {
			const displayTime = (st ) => st.slice(0, -3)
			const displayDuration = (d) => Math.floor(d/60)
			const getClassName = (index: Number) => index==this.state.selected ? "timerListItemRow-selected": "timerListItemRow-deselected"
			return timers.map( (timer: timerEvent, i: number ) => 
					<div onClick={ () => this.handleClick(i) } className={getClassName(i)} key={i}> 
						<div> {displayTime(timer.startTime)} </div>
						<div> {displayTime(timer.endTime)} </div>
						<div> {displayDuration( timer.duration) } </div> 
					</div>
				)
		}

		return (
			<div id="listContainer">
				<div className="timerListRow" id="timerListHeader" > 
					<div> start time </div> 
					<div> end time </div>
					<div> duration </div> 
				</div>
					{renderTimerList()}
				<input type="button" onClick={ this.handleDelete } value="Delete" />
			

			</div>

		)
	}
}

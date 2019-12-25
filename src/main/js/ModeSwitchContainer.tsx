import * as React from "react";
import { render as _render } from 'react-dom';
import ModeButton from './ModeButton';
import { MODES } from './flareDomain'


export default class ModeSwitchContainer extends React.Component<any, any> {

	constructor(props) {
		super(props);
	
	}

	render() {
		const { currentMode, switchMode } = this.props
		return (
            <div id="modeswitchcontainer">
				<ModeButton mode={MODES.TIMED} active={currentMode==MODES.TIMED}  switchMode={switchMode}  />
                <ModeButton mode={MODES.ON} active={currentMode==MODES.ON}  switchMode={switchMode}/>
    			<ModeButton mode={MODES.OFF} active={currentMode==MODES.OFF}  switchMode={switchMode} />
            </div>
		)
	}
}


export const MODES = {
    TIMED: "TIMED",
    OFF: "OFF",
    ON: "ON"
}

export interface timerEvent {
    startTime: string;
    duration: Number;
}
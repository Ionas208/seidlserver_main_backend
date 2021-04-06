package com.seidlserver.pojos.state;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    Created by: Jonas Seidl
    Date: 23.03.2021
    Time: 09:56
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class State {
    public StateType state;
}

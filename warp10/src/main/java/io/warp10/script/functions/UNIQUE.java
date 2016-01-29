//
//   Copyright 2016  Cityzen Data
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
//

package io.warp10.script.functions;

import io.warp10.script.NamedWarpScriptFunction;
import io.warp10.script.WarpScriptStackFunction;
import io.warp10.script.WarpScriptException;
import io.warp10.script.WarpScriptStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Make elements of a list unique by converting it to a set temporarly
 */
public class UNIQUE extends NamedWarpScriptFunction implements WarpScriptStackFunction {
  
  public UNIQUE(String name) {
    super(name);
  }
  
  @Override
  public Object apply(WarpScriptStack stack) throws WarpScriptException {        
    Object list = stack.pop();

    if (!(list instanceof List)) {
      throw new WarpScriptException(getName() + " operates on a list.");
    }

    Set<Object> set = new HashSet<Object>();
    
    set.addAll((List) list);
    
    List<Object> lst = new ArrayList<Object>();
    
    lst.addAll(set);
    set.clear();
    
    stack.push(lst);
    
    return stack;
  }
}
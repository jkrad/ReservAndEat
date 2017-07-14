/*
 * Copyright 2017 Gilberto Pacheco Gallegos.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appspot.reservandeat_171704.backend.mensajeria;

import java.util.List;
import java.util.Map;

/** @author Gilberto Pacheco Gallegos */
public class Mensaje {
  private List<String> registration_ids;
  private Map<String, String> data;
  public Mensaje() {}
  public Mensaje(List<String> registration_ids, Map<String, String> data) {
    this.registration_ids = registration_ids;
    this.data = data;
  }
  public List<String> getRegistration_ids() {
    return registration_ids;
  }
  public void setRegistration_ids(List<String> registration_ids) {
    this.registration_ids = registration_ids;
  }
  public Map<String, String> getData() {
    return data;
  }
  public void setData(Map<String, String> data) {
    this.data = data;
  }
}

/*******************************************************************************
 * Copyright 2022 Namila
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package me.namila.services.userservice.web.controller;


import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.namila.services.userservice.web.model.HealthModel;
import me.namila.services.userservice.web.utils.Constants;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = Constants.Route.HEALTH_ROUTE, produces = APPLICATION_JSON_VALUE)
public class HealthController {

  @Value("${info.version:version}")
  private String version;

  @Value("${info.description:user service}")
  private String description;

  @Value("${spring.application.name:user service}")
  private String appName;

  @GetMapping
  public Mono<ResponseEntity<EntityModel<HealthModel>>> health() {
    return Mono.defer(() -> Mono.just(new HealthModel(appName, version, description))
        .flatMap(model -> linkTo(methodOn(HealthController.class).health()).withSelfRel().toMono()
            .map(link -> model.removeLinks().add(link)))
        .map(ResponseEntity::ok));
  }


}

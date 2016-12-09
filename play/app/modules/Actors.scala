package modules

import akka.actor.ActorSystem
import javax.inject._

import actor.AdminActor
import com.google.inject.AbstractModule

/**
  * Created by Jiawei on 12/1/16.
  */
class Actors @Inject()(system: ActorSystem) extends ApplicationActors {
  system.actorOf(
    props = AdminActor.props.withDispatcher("control-aware-dispatcher"),
    name = "adminActor"
  )
}


trait ApplicationActors

class ActorsModule extends AbstractModule {

  override def configure() = {
    bind(classOf[ApplicationActors]).to(classOf[Actors]).asEagerSingleton()
  }
}


package actor.sentiment

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import message.{StartSubsribe, StopSubscribe}

import scala.concurrent.duration._

/**
  * Created by Jiawei on 12/4/16.
  */
class WebsocketActor(out: ActorRef) extends Actor with ActorLogging{

  implicit val ec = context.system.dispatcher
  val sentimentActor = context.actorSelection("akka://application/user/adminActor/sentimentActor")

  context.system.scheduler.scheduleOnce(1.second)(self ! StartSubsribe(out))

  override def receive = {
    case m @StartSubsribe(out) => sentimentActor ! m
  }

  override def postStop(): Unit = {
    sentimentActor ! StopSubscribe(out)
  }
}
//
object WebsocketActor {
  def props(out: ActorRef) = Props(classOf[WebsocketActor], out)
}

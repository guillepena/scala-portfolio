package eoi.de.examples
package patrones.monads

import java.util.Date

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

object Ejemplo2App {

  val userId: String = "diego"

  trait Booking[T] {
    def book(t: T): Future[_]
  }

  trait PlaneTickets
  trait CarRental
  trait HotelBooking
  trait WholeTripResult

  object PlaneService extends Booking[(String, String, Int)] {
    override def book(details: (String, String, Int)): Future[PlaneTickets] =
      Future {
        new PlaneTickets {
          // implementation of plane booking.
        }
      }
  }

  object CarRentalService extends Booking[PlaneTickets] {
    override def book(tickets: PlaneTickets): Future[CarRental] = Future {
      new CarRental {
        // implementation of car rental.
      }
    }
  }

  object HotelBookingService extends Booking[(String, Date, Int)] {
    override def book(details: (String, Date, Int)): Future[HotelBooking] =
      Future {
        new HotelBooking {
          // implementation of the hotel booking.
        }
      }
  }

  object HotelService extends Booking[CarRental] {
    override def book(rental: CarRental): Future[WholeTripResult] = Future {
      new WholeTripResult {
        // implementation of the booking.
      }
    }
  }

  object MonadExample {
    val resultFlatMap: Future[WholeTripResult] = PlaneService
      .book((userId, "DLT1234", 2)).flatMap(CarRentalService.book)
      .flatMap(HotelService.book)

    val resultForComp: Future[WholeTripResult] = for {
      planeTickets <- PlaneService.book((userId, "DLT1234", 2))
      carRental <- CarRentalService.book(planeTickets)
      wholeTripResult <- HotelService.book(carRental)
    } yield wholeTripResult
  }
}

object RunMonadExample extends App {
  Ejemplo2App.MonadExample.resultFlatMap.onComplete {
    case Success(wholeTripResult) =>
      println(s"Booking result (via flatMap): $wholeTripResult")
    case Failure(e) => e.printStackTrace()
  }

  Ejemplo2App.MonadExample.resultForComp.onComplete {
    case Success(wholeTripResult) =>
      println(s"Booking result (via for comprehension): $wholeTripResult")
    case Failure(e) => e.printStackTrace()
  }

  Thread.sleep(1000)
}

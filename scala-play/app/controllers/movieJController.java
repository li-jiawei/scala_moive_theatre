//package controllers;
//
//
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//
///**
// * Created by ling on 16/11/22.
// */
//public class movieJController {
//
//    @RequestMapping(value = "/searchmovie"  ,method = RequestMethod.GET)
//    public ModelAndView SearchMovie(HttpServletRequest request, HttpServletResponse response)  {
//
//        HttpSession session = request.getSession();
//        ModelAndView mv = new ModelAndView();
//
//        mv.setViewName("search");
//
//        return mv;
//    }
//
//
//
//
//}

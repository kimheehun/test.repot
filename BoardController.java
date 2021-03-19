package org.spring.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.domain.BoardVO;
import org.spring.domain.Criteria;
import org.spring.domain.MemberVO;
import org.spring.domain.PageDTO;
import org.spring.service.BoardService;
import org.spring.service.MemberService;
import org.spring.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.spring.domain.BoardAttachVO;

@Controller
@RequestMapping("board")
public class BoardController {

	@Autowired
	private BoardService service;
	// 회원가입 화면 폼
	@Autowired
	private MemberService memservice;
	
	
	private static final Logger logger=LoggerFactory.getLogger(BoardController.class);

	// 글쓰기를 위한 화면 폼
	@RequestMapping(value="/register",method = RequestMethod.GET)
	public void registerGet() throws Exception{
		logger.info("register get.......");
	}
	
	// 데이터 전달구조 > GET > BoardController > Post > Controller > View단
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String registerPOST(BoardVO board,Model model)throws Exception{
		logger.info("register post.......");
		logger.info(board.toString());
	    service.register(board);
		model.addAttribute("result","success");
		return "redirect:/board/listAll";
		// 새로고침의 문제와 리다이렉트
	}
	
	@Transactional
	@RequestMapping(value="/listAll", method=RequestMethod.GET)
	public void listAll(Model model,Criteria cri,HttpServletRequest request)throws Exception{
		// logger.info("show all list..........");
		logger.info("list Get.....",service.pagenum(cri));
		// model.addAttribute("list",service.listAll());
		model.addAttribute("list",service.pagenum(cri));
		model.addAttribute("pageMaker",new PageDTO(cri,service.getTotalCount(cri)));
		// 전체목록을 위한 컨트롤러/뷰
	}
	
	@RequestMapping(value="read",method=RequestMethod.GET)
	public void read(@RequestParam("bno")int bno, Model model)throws Exception{
		model.addAttribute(service.read(bno));
		logger.info("read Get....."+bno);
	}
	
	@RequestMapping(value="/remove",method=RequestMethod.POST)
	public String remove(@RequestParam("bno")int bno,RedirectAttributes rttr,Criteria cri)throws Exception{
		service.remove(bno);
		rttr.addFlashAttribute("msg","suceess");
		rttr.addAttribute("pageNum",cri.getPage());
		logger.info("read Post....."+bno);
		return "redirect:/board/listAll";

	}
	
	@RequestMapping(value="/modify",method=RequestMethod.GET)
	public void modifyGET(int bno, Model model)throws Exception{
		logger.info("modify Get....."+bno);
		model.addAttribute("modify",service.read(bno));
	}

	@RequestMapping(value="/modify",method=RequestMethod.POST)
	public String modifyPOST(BoardVO board, RedirectAttributes rttr,Criteria cri)throws Exception{
        
		logger.info("mod post....."+board);

		service.modify(board);
		rttr.addFlashAttribute("msg","suceess");
		rttr.addAttribute("pageNum",cri.getPage());
		return "redirect:/board/listAll";		
	}
	
	 @RequestMapping(value = "getAttachList", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	   public ResponseEntity<List<BoardAttachVO>> getAttachList(int bno){
		   logger.info("getAttachList"+bno);
		   return new ResponseEntity<>(service.getAttachList(bno),HttpStatus.OK);
	   }



}

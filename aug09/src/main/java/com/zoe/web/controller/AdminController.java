package com.zoe.web.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.zoe.web.service.AdminService;
import com.zoe.web.util.Util;

@Controller
@RequestMapping("/admin") // 어드민폴더아래 있는 애들은 이쪽으로 옴
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private Util util;

	@GetMapping("/")
	public String adminIndex2() {
		return "redirect:/admin/admin";
	}
	// adminservice /admindao / adminMapper

	// 어드민 중복으로 사용하지 않아도됨.
	@GetMapping("/admin")
	public String adminIndex() {

		return "admin/index";
		// 폴더안에 abmin폴더안에 index.jsp (jsp파일은 영향안받아서 폴더이름작아줘ㅏ야함)
	}

	@PostMapping("/login")
	public String adminLogin(@RequestParam Map<String, Object> map, HttpSession session) {
		//System.out.println(map);

		Map<String, Object> result = adminService.adminLogin(map);

		System.out.println(result);

		if (util.obj2Int(result.get("count")) == 1 && util.obj2Int(result.get("m_grade")) > 5) {
			// 세션올리기
			session.setAttribute("mid", map.get("id"));
			session.setAttribute("mname", result.get("m_name"));
			session.setAttribute("mgrade", result.get("m_grade"));
			// 메인으로 이동하기
			return "redirect:/admin/main";
		} else {
			return "redirect:/admin/admin?error=login";
		}
	}

	@GetMapping("/main")
	public String main() {
		return "admin/main"; // 폴더 적어줘야 admin/밑에 main.jsp
	}
	
	@GetMapping("/notice")
	public String notice(Model model) {
		//1 데이터베이스까지 연결하기
		//2 데이터 불러오기
		List<Map<String, Object>> list = adminService.list();
		//3 데이터 jsp로 보내기
		model.addAttribute("list", list);
		return "admin/notice";
	}
	
	@PostMapping("/noticeWrite")
	public String noticeWrite(@RequestParam("upFile") MultipartFile upfile, @RequestParam Map<String, Object> map) {
	//{title=되나요?, content=되나요, upFile=}
		//System.out.println(map);
	
		if(!upfile.isEmpty()) {
			//저장할 경로명 뽑기 request뽑기
			HttpServletRequest request = 
					((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			String path = request.getServletContext().getRealPath("/upload");
			System.out.println("실제경로 : " + path);
			
			//upfile 정보보기
			System.out.println(upfile.getOriginalFilename()); //실제 파일이름 가져오기
			System.out.println(upfile.getSize());//용량크기
			System.out.println(upfile.getContentType());//어떤 타입인지
			//진짜로 파일 업로드 하기 경로+저장할 파일명
			File newFileName = new File(upfile.getOriginalFilename());
		}
		

		
		map.put("mno", 6);
		//adminService.noticeWrite(map);
		return"redirect:/admin/notice";
			
	}
}
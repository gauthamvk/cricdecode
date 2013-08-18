package co.acjs.cricdecode;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentProviderClient;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends SherlockFragmentActivity {

	// Declare Variables
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	MenuListAdapter mMenuAdapter;
	String[] title;
	int currentFragment;
	Menu current_menu;
	static SQLiteDatabase dbHandle;

	static ContentProviderClient client;

	// Declare Constants
	static final int PROFILE_FRAGMENT = 0, CAREER_FRAGMENT = 1,
			ANALYSIS_FRAGMENT = 2, DIARY_MATCHES_FRAGMENT = 3,
			ONGOING_MATCHES_FRAGMENT = 4, MATCH_CREATION_FRAGMENT = 5,
			PERFORMANCE_FRAGMENT_EDIT = 6, PERFORMANCE_FRAGMENT_VIEW = 7;

	static {
		Log.d("Debug", "Static Initializer");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_main);

		getSupportActionBar().setDisplayShowTitleEnabled(false);

		client = getContentResolver().acquireContentProviderClient(
				CricDeCodeContentProvider.AUTHORITY);

		dbHandle = ((CricDeCodeContentProvider) client
				.getLocalContentProvider()).getDbHelper().getReadableDatabase();

		// Generate title
		title = getResources().getStringArray(R.array.drawer_list_item);

		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Pass results to MenuListAdapter Class
		mMenuAdapter = new MenuListAdapter(this, title);

		// Set the MenuListAdapter to the ListView
		mDrawerList.setAdapter(mMenuAdapter);

		// Capture button clicks on side menu
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			Log.d("Debug", "Saved is null");
			currentFragment = PROFILE_FRAGMENT;
			ProfileFragment.currentProfileFragment = ProfileFragment.PROFILE_VIEW_FRAGMENT;
			selectItem(currentFragment, true);
		} else {
			currentFragment = savedInstanceState.getInt("currentFragment");
			switch (currentFragment) {
			case PROFILE_FRAGMENT:
				ProfileFragment.profileFragment = (ProfileFragment) getSupportFragmentManager()
						.getFragment(savedInstanceState,
								"currentFragmentInstance");
				break;
			case CAREER_FRAGMENT:
				CareerFragment.careerFragment = (CareerFragment) getSupportFragmentManager()
						.getFragment(savedInstanceState,
								"currentFragmentInstance");
				break;
			case ANALYSIS_FRAGMENT:
				AnalysisFragment.analysisFragment = (AnalysisFragment) getSupportFragmentManager()
						.getFragment(savedInstanceState,
								"currentFragmentInstance");
				break;
			case MATCH_CREATION_FRAGMENT:
				MatchCreationFragment.matchCreationFragment = (MatchCreationFragment) getSupportFragmentManager()
						.getFragment(savedInstanceState,
								"currentFragmentInstance");
				break;
			case DIARY_MATCHES_FRAGMENT:
				DiaryMatchesFragment.diaryMatchesFragment = (DiaryMatchesFragment) getSupportFragmentManager()
						.getFragment(savedInstanceState,
								"currentFragmentInstance");
				break;
			case ONGOING_MATCHES_FRAGMENT:
				OngoingMatchesFragment.ongoingMatchesFragment = (OngoingMatchesFragment) getSupportFragmentManager()
						.getFragment(savedInstanceState,
								"currentFragmentInstance");
				break;
			case PERFORMANCE_FRAGMENT_EDIT:
				PerformanceFragmentEdit.performanceFragmentEdit = (PerformanceFragmentEdit) getSupportFragmentManager()
						.getFragment(savedInstanceState,
								"currentFragmentInstance");
				break;
			case PERFORMANCE_FRAGMENT_VIEW:
				PerformanceFragmentView.performanceFragmentView = (PerformanceFragmentView) getSupportFragmentManager()
						.getFragment(savedInstanceState,
								"currentFragmentInstance");
				break;
			default:
				break;
			}
			Log.d("Debug", "currentFragment " + currentFragment);
			selectItem(currentFragment, false);
		}

		// Look up the AdView as a resource and load a request. final AdView

		final AdView adView = (AdView) findViewById(R.id.adView);
		(new Thread() {
			public void run() {
				Looper.prepare();
				adView.loadAd(new AdRequest());
			}
		}).start();

	}

	@Override
	protected void onStart() {
		super.onStart();

		// Google Analytics Stop
		EasyTracker.getInstance().activityStop(this);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		switch (currentFragment) {
		case PROFILE_FRAGMENT:
			if (ProfileFragment.currentProfileFragment == ProfileFragment.PROFILE_VIEW_FRAGMENT) {
				menu.add(Menu.NONE, R.string.edit_profile, Menu.NONE,
						R.string.edit_profile);
				menu.findItem(R.string.edit_profile).setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM);

			} else {
				menu.add(Menu.NONE, R.string.save_profile, Menu.NONE,
						R.string.save_profile);
				menu.findItem(R.string.save_profile).setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM);
			}
			break;
		case MATCH_CREATION_FRAGMENT:
			menu.add(Menu.NONE, R.string.create_match, Menu.NONE,
					R.string.create_match);
			menu.findItem(R.string.create_match).setShowAsAction(
					MenuItem.SHOW_AS_ACTION_ALWAYS);
			break;
		case ANALYSIS_FRAGMENT:
		case DIARY_MATCHES_FRAGMENT:
		case CAREER_FRAGMENT:
			menu.add(Menu.NONE, R.string.filter, Menu.NONE, R.string.filter);
			menu.findItem(R.string.filter).setShowAsAction(
					MenuItem.SHOW_AS_ACTION_IF_ROOM);
			break;
		case PERFORMANCE_FRAGMENT_EDIT:
			menu.add(Menu.NONE, R.string.save_performance, Menu.NONE,
					R.string.save_performance);
			menu.findItem(R.string.save_performance).setShowAsAction(
					MenuItem.SHOW_AS_ACTION_ALWAYS);
			break;
		default:
			break;
		}
		current_menu = menu;
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("Debug", "On option item selected");
		switch (item.getItemId()) {
		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			break;
		case R.string.edit_profile:
			ProfileFragment.currentProfileFragment = ProfileFragment.PROFILE_EDIT_FRAGMENT;
			onPrepareOptionsMenu(current_menu);
			ProfileFragment.profileFragment.viewFragment();
			break;
		case R.string.save_profile:
			ProfileEditFragment.profileEditFragment.saveEditedProfile();
			ProfileFragment.currentProfileFragment = ProfileFragment.PROFILE_VIEW_FRAGMENT;
			onPrepareOptionsMenu(current_menu);
			ProfileFragment.profileFragment.viewFragment();
			break;
		case R.string.create_match:
			MatchCreationFragment.matchCreationFragment.insertMatch();
			onPrepareOptionsMenu(current_menu);
			break;
		case R.string.save_performance:
			PerformanceFragmentEdit.performanceFragmentEdit.insertOrUpdate();
			onPrepareOptionsMenu(current_menu);
			break;
		case R.string.filter:
			showFilterDialog(currentFragment);
			break;
		default:
			break;

		}
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			invalidateOptionsMenu();
		}

		return super.onOptionsItemSelected(item);
	}

	// The click listener for ListView in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (currentFragment != position) {
				currentFragment = position;
				onPrepareOptionsMenu(current_menu);
				selectItem(position, true);
			} else {
				// Close drawer
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		}
	}

	public void selectItem(int position, boolean newInstance) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Locate Position
		switch (position) {
		case PROFILE_FRAGMENT:
			Log.d("Debug", "Select Profile");
			getSupportActionBar().setDisplayShowCustomEnabled(false);
			if (newInstance) {
				ft.replace(R.id.content_frame, new ProfileFragment());
			} else {
				ft.replace(R.id.content_frame, ProfileFragment.profileFragment);
			}
			break;
		case CAREER_FRAGMENT:
			getSupportActionBar().setDisplayShowCustomEnabled(false);
			if (newInstance) {
				ft.replace(R.id.content_frame, new CareerFragment());
			} else {
				ft.replace(R.id.content_frame, CareerFragment.careerFragment);
			}
			break;
		case ANALYSIS_FRAGMENT:
			getSupportActionBar().setDisplayShowCustomEnabled(false);
			if (newInstance) {
				ft.replace(R.id.content_frame, new AnalysisFragment());
			} else {
				ft.replace(R.id.content_frame,
						AnalysisFragment.analysisFragment);
			}
			break;
		case ONGOING_MATCHES_FRAGMENT:
			getSupportActionBar().setDisplayShowCustomEnabled(false);
			if (newInstance) {
				ft.replace(R.id.content_frame, new OngoingMatchesFragment());
			} else {
				ft.replace(R.id.content_frame,
						OngoingMatchesFragment.ongoingMatchesFragment);
			}
			break;
		case DIARY_MATCHES_FRAGMENT:
			getSupportActionBar().setDisplayShowCustomEnabled(false);
			if (newInstance) {
				ft.replace(R.id.content_frame, new DiaryMatchesFragment());
			} else {
				ft.replace(R.id.content_frame,
						DiaryMatchesFragment.diaryMatchesFragment);
			}
			break;
		case MATCH_CREATION_FRAGMENT:
			getSupportActionBar().setDisplayShowCustomEnabled(false);
			if (newInstance) {
				ft.replace(R.id.content_frame, new MatchCreationFragment());
			} else {
				ft.replace(R.id.content_frame,
						MatchCreationFragment.matchCreationFragment);
			}
			break;
		case PERFORMANCE_FRAGMENT_EDIT:
			getSupportActionBar().setDisplayShowCustomEnabled(true);
			getSupportActionBar().setCustomView(R.layout.innings_spinner);
			if (newInstance) {
				ft.replace(R.id.content_frame, new PerformanceFragmentEdit());
			} else {
				ft.replace(R.id.content_frame,
						PerformanceFragmentEdit.performanceFragmentEdit);
			}
			break;
		case PERFORMANCE_FRAGMENT_VIEW:
			getSupportActionBar().setDisplayShowCustomEnabled(true);
			getSupportActionBar().setCustomView(R.layout.innings_spinner);
			if (newInstance) {
				ft.replace(R.id.content_frame, new PerformanceFragmentView());
			} else {
				ft.replace(R.id.content_frame,
						PerformanceFragmentView.performanceFragmentView);
			}
			break;
		default:
			break;
		}
		ft.commit();
		mDrawerList.setItemChecked(position, true);
		// Close drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("Debug", "Save currentFragment " + currentFragment);
		outState.putInt("currentFragment", currentFragment);

		switch (currentFragment) {
		case PROFILE_FRAGMENT:
			getSupportFragmentManager().putFragment(outState,
					"currentFragmentInstance", ProfileFragment.profileFragment);
			break;
		case CAREER_FRAGMENT:
			getSupportFragmentManager().putFragment(outState,
					"currentFragmentInstance", CareerFragment.careerFragment);
			break;
		case ANALYSIS_FRAGMENT:
			getSupportFragmentManager().putFragment(outState,
					"currentFragmentInstance",
					AnalysisFragment.analysisFragment);
			break;
		case ONGOING_MATCHES_FRAGMENT:
			getSupportFragmentManager().putFragment(outState,
					"currentFragmentInstance",
					OngoingMatchesFragment.ongoingMatchesFragment);
			break;
		case DIARY_MATCHES_FRAGMENT:
			getSupportFragmentManager().putFragment(outState,
					"currentFragmentInstance",
					DiaryMatchesFragment.diaryMatchesFragment);
			break;
		case MATCH_CREATION_FRAGMENT:
			getSupportFragmentManager().putFragment(outState,
					"currentFragmentInstance",
					MatchCreationFragment.matchCreationFragment);
			break;
		case PERFORMANCE_FRAGMENT_EDIT:
			getSupportFragmentManager().putFragment(outState,
					"currentFragmentInstance",
					PerformanceFragmentEdit.performanceFragmentEdit);
			break;
		case PERFORMANCE_FRAGMENT_VIEW:
			getSupportFragmentManager().putFragment(outState,
					"currentFragmentInstance",
					PerformanceFragmentView.performanceFragmentView);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onStop() {
		super.onStop();

		// Google Analytics Start
		EasyTracker.getInstance().activityStart(this);
	}

	public void onClick(View view) {
		final Dialog dialog;
		final View finalview;
		switch (view.getId()) {
		case R.id.make_graph_category_1:
			AnalysisFragment.analysisFragment.generateXYGraph();
			break;
		case R.id.make_graph_category_2:
			AnalysisFragment.analysisFragment.generatePieGraph();
			break;
		case R.id.date_of_birth:
			showDatePicker(R.id.date_of_birth);
			break;
		case R.id.date_of_match_row:
			showDatePicker(R.id.date_of_match);
			break;
		case R.id.profile_picture:
			ProfileEditFragment.profileEditFragment.getProfilePicture();
			break;
		case R.id.add_to_career:
			dialog = new Dialog(this);
			finalview = view;
			dialog.setContentView(R.layout.dialog_confirmation);
			dialog.setTitle("Add to Career");

			TextView dialogText = (TextView) dialog
					.findViewById(R.id.dialog_text);
			dialogText
					.setText("Are you sure you want to Add this Match to your Career?");

			Button yes = (Button) dialog.findViewById(R.id.yes);
			yes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					OngoingMatchesFragment.ongoingMatchesFragment
							.addToCareer(finalview);
					dialog.dismiss();
				}
			});

			Button no = (Button) dialog.findViewById(R.id.no);
			no.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();

			break;
		case R.id.delete_ongoing:
			dialog = new Dialog(this);
			finalview = view;
			dialog.setContentView(R.layout.dialog_confirmation);
			dialog.setTitle("Delete Match");

			dialogText = (TextView) dialog.findViewById(R.id.dialog_text);
			dialogText.setText("Are you sure you want to Delete this Match?");

			yes = (Button) dialog.findViewById(R.id.yes);
			yes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					OngoingMatchesFragment.ongoingMatchesFragment
							.deleteMatch(finalview);
					dialog.dismiss();
				}
			});

			no = (Button) dialog.findViewById(R.id.no);
			no.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
			break;
		case R.id.delete_diary:
			dialog = new Dialog(this);
			finalview = view;
			dialog.setContentView(R.layout.dialog_confirmation);
			dialog.setTitle("Delete Match");

			dialogText = (TextView) dialog.findViewById(R.id.dialog_text);
			dialogText
					.setText("Are you sure you want to Delete this Match from your Career?");

			yes = (Button) dialog.findViewById(R.id.yes);
			yes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DiaryMatchesFragment.diaryMatchesFragment
							.deleteMatch(finalview);
					dialog.dismiss();
				}
			});

			no = (Button) dialog.findViewById(R.id.no);
			no.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
			break;
		default:
			break;
		}
	}

	public void showDatePicker(int view_callee) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Create and show the dialog.
		DatePickerFragment.datePickerFragment = new DatePickerFragment();
		DatePickerFragment.datePickerFragment.setView_callee(view_callee);
		DatePickerFragment.datePickerFragment
				.setDate_str(((TextView) findViewById(view_callee)).getText()
						.toString());
		DatePickerFragment.datePickerFragment.show(ft, null);
	}

	public void showFilterDialog(int id) {
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.filter_general);
		dialog.setTitle("Filter");
		final MultiSelectSpinner season_spinner, my_team_spinner, opponent_spinner, venue_spinner, result_spinner, level_spinner, overs_spinner, innings_spinner, duration_spinner, first_spinner;
		switch (id) {
		case DIARY_MATCHES_FRAGMENT:
			season_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.season_list);
			season_spinner
					.setItems(DiaryMatchesFragment.diaryMatchesFragment.season_list);
			season_spinner
					.setSelection(DiaryMatchesFragment.diaryMatchesFragment.season_list_selected);
			season_spinner._proxyAdapter.clear();
			season_spinner._proxyAdapter.add(season_spinner
					.buildSelectedItemString());
			season_spinner.setSelection(0);

			my_team_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.my_team_list);
			my_team_spinner
					.setItems(DiaryMatchesFragment.diaryMatchesFragment.my_team_list);
			my_team_spinner
					.setSelection(DiaryMatchesFragment.diaryMatchesFragment.my_team_list_selected);
			my_team_spinner._proxyAdapter.clear();
			my_team_spinner._proxyAdapter.add(my_team_spinner
					.buildSelectedItemString());
			my_team_spinner.setSelection(0);

			opponent_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.opponent_list);
			opponent_spinner
					.setItems(DiaryMatchesFragment.diaryMatchesFragment.opponent_list);
			opponent_spinner
					.setSelection(DiaryMatchesFragment.diaryMatchesFragment.opponent_list_selected);
			opponent_spinner._proxyAdapter.clear();
			opponent_spinner._proxyAdapter.add(opponent_spinner
					.buildSelectedItemString());
			opponent_spinner.setSelection(0);

			venue_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.venue_list);
			venue_spinner
					.setItems(DiaryMatchesFragment.diaryMatchesFragment.venue_list);
			venue_spinner
					.setSelection(DiaryMatchesFragment.diaryMatchesFragment.venue_list_selected);
			venue_spinner._proxyAdapter.clear();
			venue_spinner._proxyAdapter.add(venue_spinner
					.buildSelectedItemString());
			venue_spinner.setSelection(0);

			result_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.result_list);
			result_spinner
					.setItems(DiaryMatchesFragment.diaryMatchesFragment.result_list);
			result_spinner
					.setSelection(DiaryMatchesFragment.diaryMatchesFragment.result_list_selected);
			result_spinner._proxyAdapter.clear();
			result_spinner._proxyAdapter.add(result_spinner
					.buildSelectedItemString());
			result_spinner.setSelection(0);

			level_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.level_list);
			level_spinner
					.setItems(DiaryMatchesFragment.diaryMatchesFragment.level_list);
			level_spinner
					.setSelection(DiaryMatchesFragment.diaryMatchesFragment.level_list_selected);
			level_spinner._proxyAdapter.clear();
			level_spinner._proxyAdapter.add(level_spinner
					.buildSelectedItemString());
			level_spinner.setSelection(0);

			overs_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.overs_list);
			overs_spinner
					.setItems(DiaryMatchesFragment.diaryMatchesFragment.overs_list);
			overs_spinner
					.setSelection(DiaryMatchesFragment.diaryMatchesFragment.overs_list_selected);
			overs_spinner._proxyAdapter.clear();
			overs_spinner._proxyAdapter.add(overs_spinner
					.buildSelectedItemString());
			overs_spinner.setSelection(0);

			innings_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.innings_list);
			innings_spinner
					.setItems(DiaryMatchesFragment.diaryMatchesFragment.innings_list);
			innings_spinner
					.setSelection(DiaryMatchesFragment.diaryMatchesFragment.innings_list_selected);
			innings_spinner._proxyAdapter.clear();
			innings_spinner._proxyAdapter.add(innings_spinner
					.buildSelectedItemString());
			innings_spinner.setSelection(0);

			duration_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.duration_list);
			duration_spinner
					.setItems(DiaryMatchesFragment.diaryMatchesFragment.duration_list);
			duration_spinner
					.setSelection(DiaryMatchesFragment.diaryMatchesFragment.duration_list_selected);
			duration_spinner._proxyAdapter.clear();
			duration_spinner._proxyAdapter.add(duration_spinner
					.buildSelectedItemString());
			duration_spinner.setSelection(0);

			first_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.first_list);
			first_spinner
					.setItems(DiaryMatchesFragment.diaryMatchesFragment.first_list);
			first_spinner
					.setSelection(DiaryMatchesFragment.diaryMatchesFragment.first_list_selected);
			first_spinner._proxyAdapter.clear();
			first_spinner._proxyAdapter.add(first_spinner
					.buildSelectedItemString());
			first_spinner.setSelection(0);

			Button dialogButton = (Button) dialog.findViewById(R.id.okay);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					DiaryMatchesFragment.diaryMatchesFragment.season_list_selected = season_spinner
							.getSelectedStrings();
					String str = DiaryMatchesFragment
							.buildSelectedItemString(
									DiaryMatchesFragment.diaryMatchesFragment.season_list_selected,
									false);
					if (!str.equals("")) {
						DiaryMatchesFragment.diaryMatchesFragment.season_whereClause = " and strftime('%Y',"
								+ MatchDb.KEY_MATCH_DATE + ") in(" + str + ")";
					} else {
						DiaryMatchesFragment.diaryMatchesFragment.season_whereClause = " and strftime('%Y',"
								+ MatchDb.KEY_MATCH_DATE + ") in('')";
					}

					DiaryMatchesFragment.diaryMatchesFragment.my_team_list_selected = my_team_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									DiaryMatchesFragment.diaryMatchesFragment.my_team_list_selected,
									false);
					if (!str.equals("")) {
						DiaryMatchesFragment.diaryMatchesFragment.myteam_whereClause = " and "
								+ MatchDb.KEY_MY_TEAM + " in(" + str + ")";
					} else {
						DiaryMatchesFragment.diaryMatchesFragment.myteam_whereClause = " and "
								+ MatchDb.KEY_MY_TEAM + " in('')";
					}

					DiaryMatchesFragment.diaryMatchesFragment.opponent_list_selected = opponent_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									DiaryMatchesFragment.diaryMatchesFragment.opponent_list_selected,
									false);
					if (!str.equals("")) {
						DiaryMatchesFragment.diaryMatchesFragment.opponent_whereClause = " and "
								+ MatchDb.KEY_OPPONENT_TEAM
								+ " in("
								+ str
								+ ")";
					} else {
						DiaryMatchesFragment.diaryMatchesFragment.opponent_whereClause = " and "
								+ MatchDb.KEY_OPPONENT_TEAM + " in('')";
					}

					DiaryMatchesFragment.diaryMatchesFragment.venue_list_selected = venue_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									DiaryMatchesFragment.diaryMatchesFragment.venue_list_selected,
									false);
					if (!str.equals("")) {
						DiaryMatchesFragment.diaryMatchesFragment.venue_whereClause = " and "
								+ MatchDb.KEY_VENUE + " in(" + str + ")";
					} else {
						DiaryMatchesFragment.diaryMatchesFragment.venue_whereClause = " and "
								+ MatchDb.KEY_VENUE + " in('')";
					}

					DiaryMatchesFragment.diaryMatchesFragment.result_list_selected = result_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									DiaryMatchesFragment.diaryMatchesFragment.result_list_selected,
									false);
					if (!str.equals("")) {
						DiaryMatchesFragment.diaryMatchesFragment.result_whereClause = " and "
								+ MatchDb.KEY_RESULT + " in(" + str + ")";
					} else {
						DiaryMatchesFragment.diaryMatchesFragment.result_whereClause = " and "
								+ MatchDb.KEY_RESULT + " in('')";
					}

					DiaryMatchesFragment.diaryMatchesFragment.level_list_selected = level_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									DiaryMatchesFragment.diaryMatchesFragment.level_list_selected,
									false);
					if (!str.equals("")) {
						DiaryMatchesFragment.diaryMatchesFragment.level_whereClause = " and "
								+ MatchDb.KEY_LEVEL + " in(" + str + ")";
					} else {
						DiaryMatchesFragment.diaryMatchesFragment.level_whereClause = " and "
								+ MatchDb.KEY_LEVEL + " in('')";
					}

					DiaryMatchesFragment.diaryMatchesFragment.overs_list_selected = overs_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									DiaryMatchesFragment.diaryMatchesFragment.overs_list_selected,
									true);
					if (!str.equals("")) {
						DiaryMatchesFragment.diaryMatchesFragment.overs_whereClause = " and "
								+ MatchDb.KEY_OVERS + " in(" + str + ")";
					} else {
						DiaryMatchesFragment.diaryMatchesFragment.overs_whereClause = " and "
								+ MatchDb.KEY_OVERS + " in(-2)";
					}

					DiaryMatchesFragment.diaryMatchesFragment.innings_list_selected = innings_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									DiaryMatchesFragment.diaryMatchesFragment.innings_list_selected,
									true);
					if (!str.equals("")) {
						DiaryMatchesFragment.diaryMatchesFragment.innings_whereClause = " and "
								+ MatchDb.KEY_INNINGS + " in(" + str + ")";
					} else {
						DiaryMatchesFragment.diaryMatchesFragment.innings_whereClause = " and "
								+ MatchDb.KEY_INNINGS + " in(-2)";
					}

					DiaryMatchesFragment.diaryMatchesFragment.duration_list_selected = duration_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									DiaryMatchesFragment.diaryMatchesFragment.duration_list_selected,
									false);
					if (!str.equals("")) {
						DiaryMatchesFragment.diaryMatchesFragment.duration_whereClause = " and "
								+ MatchDb.KEY_DURATION + " in(" + str + ")";
					} else {
						DiaryMatchesFragment.diaryMatchesFragment.duration_whereClause = " and "
								+ MatchDb.KEY_DURATION + " in('')";
					}

					DiaryMatchesFragment.diaryMatchesFragment.first_list_selected = first_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									DiaryMatchesFragment.diaryMatchesFragment.first_list_selected,
									false);
					if (!str.equals("")) {
						DiaryMatchesFragment.diaryMatchesFragment.first_whereClause = " and "
								+ MatchDb.KEY_FIRST_ACTION + " in(" + str + ")";
					} else {
						DiaryMatchesFragment.diaryMatchesFragment.first_whereClause = " and "
								+ MatchDb.KEY_FIRST_ACTION + " in('')";
					}

					DiaryMatchesFragment.diaryMatchesFragment
							.getSherlockActivity()
							.getSupportLoaderManager()
							.restartLoader(0, null,
									DiaryMatchesFragment.diaryMatchesFragment);
					dialog.dismiss();
				}
			});
			break;
		case CAREER_FRAGMENT:
			season_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.season_list);
			season_spinner.setItems(CareerFragment.careerFragment.season_list);
			season_spinner
					.setSelection(CareerFragment.careerFragment.season_list_selected);
			season_spinner._proxyAdapter.clear();
			season_spinner._proxyAdapter.add(season_spinner
					.buildSelectedItemString());
			season_spinner.setSelection(0);

			my_team_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.my_team_list);
			my_team_spinner
					.setItems(CareerFragment.careerFragment.my_team_list);
			my_team_spinner
					.setSelection(CareerFragment.careerFragment.my_team_list_selected);
			my_team_spinner._proxyAdapter.clear();
			my_team_spinner._proxyAdapter.add(my_team_spinner
					.buildSelectedItemString());
			my_team_spinner.setSelection(0);

			opponent_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.opponent_list);
			opponent_spinner
					.setItems(CareerFragment.careerFragment.opponent_list);
			opponent_spinner
					.setSelection(CareerFragment.careerFragment.opponent_list_selected);
			opponent_spinner._proxyAdapter.clear();
			opponent_spinner._proxyAdapter.add(opponent_spinner
					.buildSelectedItemString());
			opponent_spinner.setSelection(0);

			venue_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.venue_list);
			venue_spinner.setItems(CareerFragment.careerFragment.venue_list);
			venue_spinner
					.setSelection(CareerFragment.careerFragment.venue_list_selected);
			venue_spinner._proxyAdapter.clear();
			venue_spinner._proxyAdapter.add(venue_spinner
					.buildSelectedItemString());
			venue_spinner.setSelection(0);

			result_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.result_list);
			result_spinner.setItems(CareerFragment.careerFragment.result_list);
			result_spinner
					.setSelection(CareerFragment.careerFragment.result_list_selected);
			result_spinner._proxyAdapter.clear();
			result_spinner._proxyAdapter.add(result_spinner
					.buildSelectedItemString());
			result_spinner.setSelection(0);

			level_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.level_list);
			level_spinner.setItems(CareerFragment.careerFragment.level_list);
			level_spinner
					.setSelection(CareerFragment.careerFragment.level_list_selected);
			level_spinner._proxyAdapter.clear();
			level_spinner._proxyAdapter.add(level_spinner
					.buildSelectedItemString());
			level_spinner.setSelection(0);

			overs_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.overs_list);
			overs_spinner.setItems(CareerFragment.careerFragment.overs_list);
			overs_spinner
					.setSelection(CareerFragment.careerFragment.overs_list_selected);
			overs_spinner._proxyAdapter.clear();
			overs_spinner._proxyAdapter.add(overs_spinner
					.buildSelectedItemString());
			overs_spinner.setSelection(0);

			innings_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.innings_list);
			innings_spinner
					.setItems(CareerFragment.careerFragment.innings_list);
			innings_spinner
					.setSelection(CareerFragment.careerFragment.innings_list_selected);
			innings_spinner._proxyAdapter.clear();
			innings_spinner._proxyAdapter.add(innings_spinner
					.buildSelectedItemString());
			innings_spinner.setSelection(0);

			duration_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.duration_list);
			duration_spinner
					.setItems(CareerFragment.careerFragment.duration_list);
			duration_spinner
					.setSelection(CareerFragment.careerFragment.duration_list_selected);
			duration_spinner._proxyAdapter.clear();
			duration_spinner._proxyAdapter.add(duration_spinner
					.buildSelectedItemString());
			duration_spinner.setSelection(0);

			first_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.first_list);
			first_spinner.setItems(CareerFragment.careerFragment.first_list);
			first_spinner
					.setSelection(CareerFragment.careerFragment.first_list_selected);
			first_spinner._proxyAdapter.clear();
			first_spinner._proxyAdapter.add(first_spinner
					.buildSelectedItemString());
			first_spinner.setSelection(0);

			dialogButton = (Button) dialog.findViewById(R.id.okay);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					CareerFragment.careerFragment.season_list_selected = season_spinner
							.getSelectedStrings();
					String str = DiaryMatchesFragment.buildSelectedItemString(
							CareerFragment.careerFragment.season_list_selected,
							false);
					if (!str.equals("")) {
						CareerFragment.careerFragment.season_whereClause = " and strftime('%Y',m."
								+ MatchDb.KEY_MATCH_DATE + ") in(" + str + ")";
					} else {
						CareerFragment.careerFragment.season_whereClause = " and strftime('%Y',m."
								+ MatchDb.KEY_MATCH_DATE + ") in('')";
					}

					CareerFragment.careerFragment.my_team_list_selected = my_team_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									CareerFragment.careerFragment.my_team_list_selected,
									false);
					if (!str.equals("")) {
						CareerFragment.careerFragment.myteam_whereClause = " and m."
								+ MatchDb.KEY_MY_TEAM + " in(" + str + ")";
					} else {
						CareerFragment.careerFragment.myteam_whereClause = " and m."
								+ MatchDb.KEY_MY_TEAM + " in('')";
					}

					CareerFragment.careerFragment.opponent_list_selected = opponent_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									CareerFragment.careerFragment.opponent_list_selected,
									false);
					if (!str.equals("")) {
						CareerFragment.careerFragment.opponent_whereClause = " and m."
								+ MatchDb.KEY_OPPONENT_TEAM
								+ " in("
								+ str
								+ ")";
					} else {
						CareerFragment.careerFragment.opponent_whereClause = " and m."
								+ MatchDb.KEY_OPPONENT_TEAM + " in('')";
					}

					CareerFragment.careerFragment.venue_list_selected = venue_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment.buildSelectedItemString(
							CareerFragment.careerFragment.venue_list_selected,
							false);
					if (!str.equals("")) {
						CareerFragment.careerFragment.venue_whereClause = " and m."
								+ MatchDb.KEY_VENUE + " in(" + str + ")";
					} else {
						CareerFragment.careerFragment.venue_whereClause = " and m."
								+ MatchDb.KEY_VENUE + " in('')";
					}

					CareerFragment.careerFragment.result_list_selected = result_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment.buildSelectedItemString(
							CareerFragment.careerFragment.result_list_selected,
							false);
					if (!str.equals("")) {
						CareerFragment.careerFragment.result_whereClause = " and m."
								+ MatchDb.KEY_RESULT + " in(" + str + ")";
					} else {
						CareerFragment.careerFragment.result_whereClause = " and m."
								+ MatchDb.KEY_RESULT + " in('')";
					}

					CareerFragment.careerFragment.level_list_selected = level_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment.buildSelectedItemString(
							CareerFragment.careerFragment.level_list_selected,
							false);
					if (!str.equals("")) {
						CareerFragment.careerFragment.level_whereClause = " and m."
								+ MatchDb.KEY_LEVEL + " in(" + str + ")";
					} else {
						CareerFragment.careerFragment.level_whereClause = " and m."
								+ MatchDb.KEY_LEVEL + " in('')";
					}

					CareerFragment.careerFragment.overs_list_selected = overs_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment.buildSelectedItemString(
							CareerFragment.careerFragment.overs_list_selected,
							true);
					if (!str.equals("")) {
						CareerFragment.careerFragment.overs_whereClause = " and m."
								+ MatchDb.KEY_OVERS + " in(" + str + ")";
					} else {
						CareerFragment.careerFragment.overs_whereClause = " and m."
								+ MatchDb.KEY_OVERS + " in(-2)";
					}

					CareerFragment.careerFragment.innings_list_selected = innings_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									CareerFragment.careerFragment.innings_list_selected,
									true);
					if (!str.equals("")) {
						CareerFragment.careerFragment.innings_whereClause = " and m."
								+ MatchDb.KEY_INNINGS + " in(" + str + ")";
					} else {
						CareerFragment.careerFragment.innings_whereClause = " and m."
								+ MatchDb.KEY_INNINGS + " in(-2)";
					}

					CareerFragment.careerFragment.duration_list_selected = duration_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									CareerFragment.careerFragment.duration_list_selected,
									false);
					if (!str.equals("")) {
						CareerFragment.careerFragment.duration_whereClause = " and m."
								+ MatchDb.KEY_DURATION + " in(" + str + ")";
					} else {
						CareerFragment.careerFragment.duration_whereClause = " and m."
								+ MatchDb.KEY_DURATION + " in('')";
					}

					CareerFragment.careerFragment.first_list_selected = first_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment.buildSelectedItemString(
							CareerFragment.careerFragment.first_list_selected,
							false);
					if (!str.equals("")) {
						CareerFragment.careerFragment.first_whereClause = " and m."
								+ MatchDb.KEY_FIRST_ACTION + " in(" + str + ")";
					} else {
						CareerFragment.careerFragment.first_whereClause = " and m."
								+ MatchDb.KEY_FIRST_ACTION + " in('')";
					}

					// view Info
					CareerFragment.careerFragment.fireQueries();
					CareerFragment.careerFragment
							.viewInfo(CareerFragment.careerFragment.mTabHost
									.getCurrentTab());

					dialog.dismiss();
				}
			});
			break;
		case ANALYSIS_FRAGMENT:
			season_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.season_list);
			season_spinner
					.setItems(AnalysisFragment.analysisFragment.season_list);
			season_spinner
					.setSelection(AnalysisFragment.analysisFragment.season_list_selected);
			season_spinner._proxyAdapter.clear();
			season_spinner._proxyAdapter.add(season_spinner
					.buildSelectedItemString());
			season_spinner.setSelection(0);

			my_team_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.my_team_list);
			my_team_spinner
					.setItems(AnalysisFragment.analysisFragment.my_team_list);
			my_team_spinner
					.setSelection(AnalysisFragment.analysisFragment.my_team_list_selected);
			my_team_spinner._proxyAdapter.clear();
			my_team_spinner._proxyAdapter.add(my_team_spinner
					.buildSelectedItemString());
			my_team_spinner.setSelection(0);

			opponent_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.opponent_list);
			opponent_spinner
					.setItems(AnalysisFragment.analysisFragment.opponent_list);
			opponent_spinner
					.setSelection(AnalysisFragment.analysisFragment.opponent_list_selected);
			opponent_spinner._proxyAdapter.clear();
			opponent_spinner._proxyAdapter.add(opponent_spinner
					.buildSelectedItemString());
			opponent_spinner.setSelection(0);

			venue_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.venue_list);
			venue_spinner
					.setItems(AnalysisFragment.analysisFragment.venue_list);
			venue_spinner
					.setSelection(AnalysisFragment.analysisFragment.venue_list_selected);
			venue_spinner._proxyAdapter.clear();
			venue_spinner._proxyAdapter.add(venue_spinner
					.buildSelectedItemString());
			venue_spinner.setSelection(0);

			result_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.result_list);
			result_spinner
					.setItems(AnalysisFragment.analysisFragment.result_list);
			result_spinner
					.setSelection(AnalysisFragment.analysisFragment.result_list_selected);
			result_spinner._proxyAdapter.clear();
			result_spinner._proxyAdapter.add(result_spinner
					.buildSelectedItemString());
			result_spinner.setSelection(0);

			level_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.level_list);
			level_spinner
					.setItems(AnalysisFragment.analysisFragment.level_list);
			level_spinner
					.setSelection(AnalysisFragment.analysisFragment.level_list_selected);
			level_spinner._proxyAdapter.clear();
			level_spinner._proxyAdapter.add(level_spinner
					.buildSelectedItemString());
			level_spinner.setSelection(0);

			overs_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.overs_list);
			overs_spinner
					.setItems(AnalysisFragment.analysisFragment.overs_list);
			overs_spinner
					.setSelection(AnalysisFragment.analysisFragment.overs_list_selected);
			overs_spinner._proxyAdapter.clear();
			overs_spinner._proxyAdapter.add(overs_spinner
					.buildSelectedItemString());
			overs_spinner.setSelection(0);

			innings_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.innings_list);
			innings_spinner
					.setItems(AnalysisFragment.analysisFragment.innings_list);
			innings_spinner
					.setSelection(AnalysisFragment.analysisFragment.innings_list_selected);
			innings_spinner._proxyAdapter.clear();
			innings_spinner._proxyAdapter.add(innings_spinner
					.buildSelectedItemString());
			innings_spinner.setSelection(0);

			duration_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.duration_list);
			duration_spinner
					.setItems(AnalysisFragment.analysisFragment.duration_list);
			duration_spinner
					.setSelection(AnalysisFragment.analysisFragment.duration_list_selected);
			duration_spinner._proxyAdapter.clear();
			duration_spinner._proxyAdapter.add(duration_spinner
					.buildSelectedItemString());
			duration_spinner.setSelection(0);

			first_spinner = (MultiSelectSpinner) dialog
					.findViewById(R.id.first_list);
			first_spinner
					.setItems(AnalysisFragment.analysisFragment.first_list);
			first_spinner
					.setSelection(AnalysisFragment.analysisFragment.first_list_selected);
			first_spinner._proxyAdapter.clear();
			first_spinner._proxyAdapter.add(first_spinner
					.buildSelectedItemString());
			first_spinner.setSelection(0);

			dialogButton = (Button) dialog.findViewById(R.id.okay);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					AnalysisFragment.analysisFragment.season_list_selected = season_spinner
							.getSelectedStrings();
					String str = DiaryMatchesFragment
							.buildSelectedItemString(
									AnalysisFragment.analysisFragment.season_list_selected,
									false);
					if (!str.equals("")) {
						AnalysisFragment.analysisFragment.season_whereClause = " and strftime('%Y',m."
								+ MatchDb.KEY_MATCH_DATE + ") in(" + str + ")";
					} else {
						AnalysisFragment.analysisFragment.season_whereClause = " and strftime('%Y',m."
								+ MatchDb.KEY_MATCH_DATE + ") in('')";
					}

					AnalysisFragment.analysisFragment.my_team_list_selected = my_team_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									AnalysisFragment.analysisFragment.my_team_list_selected,
									false);
					if (!str.equals("")) {
						AnalysisFragment.analysisFragment.myteam_whereClause = " and m."
								+ MatchDb.KEY_MY_TEAM + " in(" + str + ")";
					} else {
						AnalysisFragment.analysisFragment.myteam_whereClause = " and m."
								+ MatchDb.KEY_MY_TEAM + " in('')";
					}

					AnalysisFragment.analysisFragment.opponent_list_selected = opponent_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									AnalysisFragment.analysisFragment.opponent_list_selected,
									false);
					if (!str.equals("")) {
						AnalysisFragment.analysisFragment.opponent_whereClause = " and m."
								+ MatchDb.KEY_OPPONENT_TEAM
								+ " in("
								+ str
								+ ")";
					} else {
						AnalysisFragment.analysisFragment.opponent_whereClause = " and m."
								+ MatchDb.KEY_OPPONENT_TEAM + " in('')";
					}

					AnalysisFragment.analysisFragment.venue_list_selected = venue_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									AnalysisFragment.analysisFragment.venue_list_selected,
									false);
					if (!str.equals("")) {
						AnalysisFragment.analysisFragment.venue_whereClause = " and m."
								+ MatchDb.KEY_VENUE + " in(" + str + ")";
					} else {
						AnalysisFragment.analysisFragment.venue_whereClause = " and m."
								+ MatchDb.KEY_VENUE + " in('')";
					}

					AnalysisFragment.analysisFragment.result_list_selected = result_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									AnalysisFragment.analysisFragment.result_list_selected,
									false);
					if (!str.equals("")) {
						AnalysisFragment.analysisFragment.result_whereClause = " and m."
								+ MatchDb.KEY_RESULT + " in(" + str + ")";
					} else {
						AnalysisFragment.analysisFragment.result_whereClause = " and m."
								+ MatchDb.KEY_RESULT + " in('')";
					}

					AnalysisFragment.analysisFragment.level_list_selected = level_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									AnalysisFragment.analysisFragment.level_list_selected,
									false);
					if (!str.equals("")) {
						AnalysisFragment.analysisFragment.level_whereClause = " and m."
								+ MatchDb.KEY_LEVEL + " in(" + str + ")";
					} else {
						AnalysisFragment.analysisFragment.level_whereClause = " and m."
								+ MatchDb.KEY_LEVEL + " in('')";
					}

					AnalysisFragment.analysisFragment.overs_list_selected = overs_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									AnalysisFragment.analysisFragment.overs_list_selected,
									true);
					if (!str.equals("")) {
						AnalysisFragment.analysisFragment.overs_whereClause = " and m."
								+ MatchDb.KEY_OVERS + " in(" + str + ")";
					} else {
						AnalysisFragment.analysisFragment.overs_whereClause = " and m."
								+ MatchDb.KEY_OVERS + " in(-2)";
					}

					AnalysisFragment.analysisFragment.innings_list_selected = innings_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									AnalysisFragment.analysisFragment.innings_list_selected,
									true);
					if (!str.equals("")) {
						AnalysisFragment.analysisFragment.innings_whereClause = " and m."
								+ MatchDb.KEY_INNINGS + " in(" + str + ")";
					} else {
						AnalysisFragment.analysisFragment.innings_whereClause = " and m."
								+ MatchDb.KEY_INNINGS + " in(-2)";
					}

					AnalysisFragment.analysisFragment.duration_list_selected = duration_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									AnalysisFragment.analysisFragment.duration_list_selected,
									false);
					if (!str.equals("")) {
						AnalysisFragment.analysisFragment.duration_whereClause = " and m."
								+ MatchDb.KEY_DURATION + " in(" + str + ")";
					} else {
						AnalysisFragment.analysisFragment.duration_whereClause = " and m."
								+ MatchDb.KEY_DURATION + " in('')";
					}

					AnalysisFragment.analysisFragment.first_list_selected = first_spinner
							.getSelectedStrings();
					str = DiaryMatchesFragment
							.buildSelectedItemString(
									AnalysisFragment.analysisFragment.first_list_selected,
									false);
					if (!str.equals("")) {
						AnalysisFragment.analysisFragment.first_whereClause = " and m."
								+ MatchDb.KEY_FIRST_ACTION + " in(" + str + ")";
					} else {
						AnalysisFragment.analysisFragment.first_whereClause = " and m."
								+ MatchDb.KEY_FIRST_ACTION + " in('')";
					}

					dialog.dismiss();
				}
			});
			break;
		default:
			break;
		}
		dialog.show();
	}
}